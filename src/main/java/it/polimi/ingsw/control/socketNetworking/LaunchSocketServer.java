package it.polimi.ingsw.control.socketNetworking;

import it.polimi.ingsw.control.socketNetworking.network.SagradaSocketServer;
import it.polimi.ingsw.model.constants.SocketConstants;

import java.io.IOException;

public class LaunchSocketServer {

    public static void main(String[] args) throws IOException {
        SagradaSocketServer server = new SagradaSocketServer(SocketConstants.SOCKET_SERVER_PORT);

        try {
            server.run();
        } finally {
            server.close();
        }
    }
}
