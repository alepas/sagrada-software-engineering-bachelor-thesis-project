package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements NetworkClient {
    private final String host;
    private final int port;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread receiver;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        connection = new Socket(host, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    public Response request(Request request) {
        try {
            out.writeObject(request);
            return ((Response) in.readObject());
        } catch (IOException e) {
            throw new RuntimeException("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Wrong deserialization: " + e.getMessage());
        }
    }

    @Override
    public void startPlaying(ResponseHandler handler, String gameID) {
        // start a receiver thread
        receiver = new Thread(
                () -> {
                    Response response = null;
                    do {
                        try {
                            response = (Response) in.readObject();
                            if (response != null) {
                                response.handle(handler);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } while (response != null);
                }
        );
        receiver.start();
    }
}
