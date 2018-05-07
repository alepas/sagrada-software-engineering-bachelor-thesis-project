package it.polimi.ingsw.view.gui.guimodel;

import it.polimi.ingsw.control.ClientController;
import it.polimi.ingsw.control.network.rmi.RmiClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.constants.NetworkConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class GuiMain extends Application {




        private static Scanner scanner = new Scanner(System.in);

        private enum Technology { SOCKET, RMI }

        public static void main(String[] args) throws IOException {
            String answer;
            Technology technology = null;

            do {
                System.out.println(">>> Quale tecnologia vuoi usare? socket/rmi");
                System.out.println(">>> Digita \"quit\" per uscire");
                answer = userInput().toLowerCase();
                if (answer.equals("socket")) technology = Technology.SOCKET;
                if (answer.equals("rmi")) technology = Technology.RMI;
                if (answer.equals("quit")) break;
                if (technology == null) {
                    System.out.println(">>> Istruzione non riconosciuta. Perfavore inserisci \"socket\" o \"rmi\"");
                    continue;
                }

                switch (technology){
                    case SOCKET:
                        try {
                            startSocketClient();
                        } catch (ConnectException e) {
                            System.out.println(">>> Impossibile stabilire connessione Socket con il Server");
                            technology = null;
                        }
                        break;
                    case RMI:
                        try {
                            startRmiClient();
                        } catch (Exception e){
                            System.out.println(">>> Impossibile stabilire connessione RMI con il Server");
                            e.printStackTrace();
                            technology = null;
                        }
                        break;
                    default:
                        System.out.println(">>> Tecnologia " + technology.toString() + " non ancora supportata");
                        technology = null;
                        break;
                }
            } while (technology == null);
            Application.launch(GuiMain.class, args);
        }

        private static void startSocketClient() throws IOException{
            SocketClient client = new SocketClient(
                    NetworkConstants.SERVER_ADDRESS, NetworkConstants.SOCKET_SERVER_PORT);

            client.init();
            ClientController controller = ClientController.getInstance(client);
            controller.run();

            client.close();
        }


        private static void startRmiClient() throws Exception {
            RmiClient client = new RmiClient();
            ClientController controller = ClientController.getInstance(client);
            controller.run();
        }


        private static String userInput(){
            return scanner.nextLine();
        }


    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/view/gui/guiview/gui.fxml"));
        stage.setTitle("Sagrada");
        stage.setScene(new Scene(root));
        stage.show();
    }


}
