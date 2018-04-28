package it.polimi.ingsw.control.socketNetworking.network;

import it.polimi.ingsw.control.socketNetworking.SocketServerController;
import it.polimi.ingsw.control.socketNetworking.network.commands.Request;
import it.polimi.ingsw.control.socketNetworking.network.commands.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketClientHandler implements Runnable{
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean stop;

    private final SocketServerController controller;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        this.controller = new SocketServerController(this);
    }

    public void respond(Response response) {
        try {
            out.writeObject(response);
        } catch (IOException e) {
            printError("IO - " + e.getMessage());
        }
    }

    private void printError(String message) {
        System.err.println(">>> ERROR@" + socket.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void run() {
        try {
            do {
                Response response = ((Request) in.readObject()).handle(controller);
                if (response != null) {
                    respond(response);
                }
            } while (!stop);
        } catch (Exception e) {
//            System.out.println("Appena prima");
//            printError(e.getClass().getSimpleName() + " - " + e.getMessage());
//            System.out.println("Appena dopo");
            close();
        }

        close();
    }

    public void stop(){ stop = true; }

    private void close(){
        stop = true;
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                printError("Errors in closing - " + e.getMessage());
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                printError("Errors in closing - " + e.getMessage());
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            printError("Errors in closing - " + e.getMessage());
        }
    }


    //Mancano altri medoti, ricontrollare poi
}
