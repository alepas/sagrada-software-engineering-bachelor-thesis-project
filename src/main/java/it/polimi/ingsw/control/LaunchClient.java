package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.constants.SocketConstants;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    private static Scanner scanner = new Scanner(System.in);

    private enum Tecnology { SOCKET, RMI }

    public static void main(String[] args) throws IOException {
        String answer;
        Tecnology tecnology = null;

        do {
            System.out.println("Quale tecnologia vuoi usare? socket/rmi");
            answer = userInput().toLowerCase();
            if (answer.equals("socket")) tecnology = Tecnology.SOCKET;
            if (answer.equals("rmi")) tecnology = Tecnology.RMI;
            if (tecnology == null) System.out.println("Istruzione non riconosciuta. Perfavore inserisci \"socket\" o \"rmi\"");
        } while (tecnology == null);

        switch (tecnology){
            case SOCKET:
                startSocketClient();
                break;
            case RMI:
                startRmiClient();
        }


    }

    private static void startSocketClient() throws IOException{
        SocketClient client = new SocketClient(
                SocketConstants.SOCKET_SERVER_ADDRESS, SocketConstants.SOCKET_SERVER_PORT);

        client.init();
        ClientController controller = new ClientController(client);
        controller.run();

        client.close();
    }


    private static void startRmiClient() {

    }


    private static String userInput(){
        return scanner.nextLine();
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