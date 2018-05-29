package it.polimi.ingsw.control.guicontroller;


import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.constants.NetworkConstants;

import it.polimi.ingsw.view.cli.LaunchCli;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.ConnectException;


public class StartController {

    @FXML private Button cliButton;

    @FXML private Button guiButton;

    @FXML
    private Button rmiButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Button socketButton;


    public void initialize() {
        cliButton.setOnAction(event -> {
            Stage window = (Stage) cliButton.getScene().getWindow();
            window.close();
            try {
                LaunchCli.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        guiButton.setOnAction(event -> {
            guiButton.setVisible(false);
            cliButton.setVisible(false);
            rmiButton.setVisible(true);
            socketButton.setVisible(true);
        });

        rmiButton.setOnAction(event -> {
            try {
                NetworkClient.getNewRmiInstance();
            } catch (Exception e) {
                System.out.println(">>> Impossibile stabilire connessione RMI con il Server");
                e.printStackTrace();
            }
            changeButton();
        });

        socketButton.setOnAction(event -> {
            try {
                startSocketClient();
            } catch (ConnectException e) {
                System.out.println(">>> Impossibile stabilire connessione Socket con il Server");
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeButton();
        });

        signUpButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SignUpScene.fxml"));

        signInButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SignInScene.fxml"));
    }

    private static void startSocketClient() throws IOException {
        NetworkClient networkClient = NetworkClient.getNewSocketInstance(NetworkConstants.SERVER_ADDRESS,
                NetworkConstants.SOCKET_SERVER_PORT);

        ((SocketClient) networkClient).init();
        //((SocketClient) networkClient).close();
    }

    private void changeButton(){
        rmiButton.setVisible(false);
        socketButton.setVisible(false);
        signInButton.setVisible(true);
        signUpButton.setVisible(true);
    }

    private void changeSceneHandle(ActionEvent event, String path) {
        AnchorPane nextNode = new AnchorPane();
        try {
            nextNode = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
