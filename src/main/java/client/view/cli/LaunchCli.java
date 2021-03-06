package client.view.cli;

import client.controller.CliController;
import client.network.NetworkClient;
import client.network.RmiClient;
import client.network.SocketClient;
import shared.constants.NetworkConstants;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

import static client.constants.CliConstants.*;

public class LaunchCli {
    private static Scanner scanner = new Scanner(System.in);

    private enum Tecnology { SOCKET, RMI }

    /**
     * asks to the player how he/she wants to connect and, depending on the answer calls different methods.
     *
     * @throws IOException if there are problems in create the new connection
     */
    public static void main() throws IOException {
        String answer;
        Tecnology tecnology = null;
        boolean quit = false;

        do {
            System.out.println(SOCKET_OR_RMI + PRESENT_QUIT);
            answer = userInput().toLowerCase();
            if (SOCKET_RESPONSES.contains(answer)) tecnology = Tecnology.SOCKET;
            if (RMI_RESPONSES.contains(answer)) tecnology = Tecnology.RMI;
            if (QUIT_RESPONSES.contains(answer)) return;
            if (tecnology == null) {
                System.out.println();
                continue;
            }

            switch (tecnology){
                case SOCKET:
                    try {
                        quit = startSocketClient();
                    } catch (ConnectException e) {
                        System.out.println(CANNOT_CONNECT_WITH_SOCKET_SERVER);
                        tecnology = null;
                    }
                    break;
                case RMI:
                    try {
                        quit = startRmiClient();
                    } catch (Exception e){
                        System.out.println(CANNOT_CONNECT_WITH_RMI_SERVER);
                        tecnology = null;
                    }
                    break;
                default:
                    System.out.println(TECHNOLOGY_NOT_SUPPORTED_P1 + tecnology.toString() + TECHNOLOGY_NOT_SUPPORTED_P2);
                    tecnology = null;
                    break;
            }
        } while (!quit);
    }

    /**
     * @return true is the socket connection started in a correct way
     *
     * @throws IOException if it not possible to add start a new socket connection
     */
    private static boolean startSocketClient() throws IOException {
        SocketClient client = NetworkClient.getNewSocketInstance(
                NetworkConstants.SERVER_ADDRESS, NetworkConstants.SOCKET_SERVER_PORT);

        client.init();
        CliController controller = new CliController(client);
        boolean quit = controller.run();

        client.close();
        return quit;
    }


    /**
     * @return true if the connection started in a correct way
     *
     * @throws Exception is it not possible to add an rmi connection
     */
    private static boolean startRmiClient() throws Exception {
        RmiClient client = NetworkClient.getNewRmiInstance();
        CliController controller = new CliController(client);
        return controller.run();
    }


    private static String userInput(){
        return scanner.nextLine();
    }
}