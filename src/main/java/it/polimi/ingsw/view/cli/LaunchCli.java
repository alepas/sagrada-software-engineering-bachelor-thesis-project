package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.rmi.RmiClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.constants.NetworkConstants;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class LaunchCli {
    private static Scanner scanner = new Scanner(System.in);

    private enum Tecnology { SOCKET, RMI }

    public static void main() throws IOException {
        String answer;
        Tecnology tecnology = null;

        do {
            System.out.println(">>> Vuoi usare socket o rmi? (Digita \"quit\" per uscire)");
            answer = userInput().toLowerCase();
            if (answer.equals("socket") || answer.equals("s")) tecnology = Tecnology.SOCKET;
            if (answer.equals("rmi") || answer.equals("r")) tecnology = Tecnology.RMI;
            if (answer.equals("quit") || answer.equals("q")) return;
            if (tecnology == null) {
                System.out.println(">>> Istruzione non riconosciuta. Perfavore inserisci \"socket\" o \"rmi\"");
                continue;
            }

            switch (tecnology){
                case SOCKET:
                    try {
                        startSocketClient();
                    } catch (ConnectException e) {
                        System.out.println(">>> Impossibile stabilire connessione Socket con il Server");
                        tecnology = null;
                    }
                    break;
                case RMI:
                    try {
                        startRmiClient();
                    } catch (Exception e){
                        System.out.println(">>> Impossibile stabilire connessione RMI con il Server");
                        e.printStackTrace();
                        tecnology = null;
                    }
                    break;
                default:
                    System.out.println(">>> Tecnologia " + tecnology.toString() + " non ancora supportata");
                    tecnology = null;
                    break;
            }
        } while (tecnology == null);
    }

    private static void startSocketClient() throws IOException {
        SocketClient client = NetworkClient.getNewSocketInstance(
                NetworkConstants.SERVER_ADDRESS, NetworkConstants.SOCKET_SERVER_PORT);

        client.init();
        CliController controller = new CliController(client);
        controller.run();

        client.close();
    }


    private static void startRmiClient() throws Exception {
        RmiClient client = NetworkClient.getNewRmiInstance();
        CliController controller = new CliController(client);
        controller.run();
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