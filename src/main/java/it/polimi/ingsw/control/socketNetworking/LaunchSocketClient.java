package it.polimi.ingsw.control.socketNetworking;

import it.polimi.ingsw.control.socketNetworking.network.SagradaSocketClient;
import it.polimi.ingsw.model.constants.SocketConstants;

import java.io.IOException;

public class LaunchSocketClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        SagradaSocketClient client = new SagradaSocketClient(
                SocketConstants.SOCKET_SERVER_ADDRESS, SocketConstants.SOCKET_SERVER_PORT);

        client.init();
        SocketClientController controller = new SocketClientController(client);
        controller.run();

        client.close();
    }
}


//        if (args.length == 0) {
//            System.err.println("Provide host:port please");
//            return;
//        }
//
//        String[] tokens = args[0].split(":");
//
//        if (tokens.length < 2) {
//            throw new IllegalArgumentException("Bad formatting: " + args[0]);
//        }
//
//        String host = tokens[0];
//        int port = Integer.parseInt(tokens[1]);

//        Client client = new Client(host, port);