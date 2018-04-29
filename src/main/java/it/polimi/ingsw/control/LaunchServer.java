package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.socket.SocketServer;
import it.polimi.ingsw.model.constants.SocketConstants;

import java.io.IOException;

public class LaunchServer {

    public static void main(String[] args) throws IOException {
        //TODO: RmiServer
        SocketServer server = new SocketServer(SocketConstants.SOCKET_SERVER_PORT);

        try {
            server.run();
        } finally {
            server.close();
        }
    }
}
