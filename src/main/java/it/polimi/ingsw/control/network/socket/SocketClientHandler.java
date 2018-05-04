package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;
import it.polimi.ingsw.model.game.gameObservers.socketNotifications.PlayersChangedNotification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketClientHandler implements Runnable, GameObserver {
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean stop;

    private final ServerController controller;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        this.controller = new ServerController(this);
    }

    public Socket getSocket() {
        return socket;
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
//            printError(e.getClass().getSimpleName() + " - " + e.getMessage());
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


    //------------------------------ Game observer ------------------------------

    @Override
    public void onJoin(String username) {
        respond(new PlayersChangedNotification(username, true));
    }

    @Override
    public void onLeave(String username) {
        respond(new PlayersChangedNotification(username, false));
    }
}
