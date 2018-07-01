package client.controller.guicontroller;

import client.network.ClientInfo;
import client.network.NetworkClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import shared.clientInfo.NextAction;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import shared.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class SignInController {

    private NetworkClient networkClient;
    private ClientInfo clientInfo;

    @FXML private TextField signInUsername;

    @FXML private PasswordField signInPassword;

    @FXML private Button signInButton;

    @FXML private Button backButton;

    @FXML private Label signInErrorLabel;


    /**
     *Initializes the sign in scene, contains the lambdas related to the button in the main scene.
     */
    public void initialize( )  {
        networkClient = NetworkClient.getInstance();
        clientInfo = ClientInfo.getInstance();

        signInButton.setOnAction(event-> {

                String username = signInUsername.getText();
                String password = signInPassword.getText();

                if (!password.equals("") || !username.equals(""))
                    loginUser(username, password, event);
                else signInErrorLabel.setVisible(true);
        });

        signInUsername.setOnMouseClicked(event -> signInErrorLabel.setVisible(false));

        backButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/ChooseHowToSignScene.fxml"));
    }


    /**
     * Calls the login method in the network. If the clientInfo username is null the player must re-introduce
     * both username and password because he/she could have done a mistake; if it is different from null it calls the
     * next scene.
     *
     * @param username is the string inserted by the player
     * @param password is the password string inserted by the player
     * @param event is the event related to the action done by the user on the login button
     */
    private void loginUser(String username, String password, ActionEvent event) {
        Thread signIn = new Thread(()->{
            try {
                networkClient.login(username, password);
                sleep(500);
                Platform.runLater(()->{
                    try {
                        networkClient.getUpdatedGame(clientInfo.getUserToken());
                        if (clientInfo.getGame() != null) {
                            if (clientInfo.getMyWpc() != null) chooseGameScene(event);
                            else changeSceneHandle(event, "/view/gui/SetNewGameScene.fxml");
                        }
                    } catch (CannotFindPlayerInDatabaseException e) {
                        changeSceneHandle(event, "/view/gui/SetNewGameScene.fxml");
                    }
                });
            } catch (CannotLoginUserException e) {
                Platform.runLater(()->{
                    signInErrorLabel.setText(e.getMessage());
                    signInErrorLabel.setVisible(true);
                    signInUsername.clear();
                    signInPassword.clear();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        signIn.start();
    }

    /**
     * Depending on the players' number a corresponding game scene will be set.
     *
     * @param event is the event that generated the login
     */
    private void chooseGameScene(ActionEvent event) {
        Platform.runLater(()-> {
            int numPlayers = clientInfo.getGameNumPlayers();
            switch (numPlayers) {
                case 1:
                    break;
                case 2:
                    changeSceneHandle(event, "/view/gui/TwoPlayersGameScene.fxml");
                    break;
                case 3:
                    changeSceneHandle(event, "/view/gui/ThreePlayersGameScene.fxml");
                    break;
                case 4:
                    changeSceneHandle(event, "/view/gui/FourPlayersGameScene.fxml");
                    break;

            }
        });
    }


    /**
     * Changes the scene in the window.
     *
     * @param event the event related to the desire of the player to change scene
     * @param path is the path of the next scene
     */
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
