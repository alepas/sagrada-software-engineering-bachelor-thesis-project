package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindGameForUserInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
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

import java.io.IOException;

public class SignInController {

    private NetworkClient networkClient;
    private ClientModel clientModel;

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
        clientModel = ClientModel.getInstance();

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
     * Calls the login method in the network. If the clientModel username is null the player must re-introduce
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
                Platform.runLater(()->{
                    try {
                        int numPlayers = networkClient.findAlreadyStartedGame(clientModel.getUserToken());
                        System.out.println(numPlayers);
                        switch (numPlayers){
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
                    } catch (CannotFindGameForUserInDatabaseException e) {
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
            }

        });
        signIn.start();
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
