package it.polimi.ingsw.client.controller.guicontroller;

import it.polimi.ingsw.client.network.NetworkClient;
import it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
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


public class SignUpController {


    private NetworkClient networkClient;

    @FXML private PasswordField signUpPassword;

    @FXML private Button backButton;

    @FXML private Button disconnectButton;

    @FXML private Button signUpButton;

    @FXML private TextField signUpUsername;

    @FXML private Label signUpErrorLabel;

    /**
     *Initializes the sign up scene, contains the lambdas related to the button in the main scene.
     */
    public void  initialize(){
        networkClient = NetworkClient.getInstance();

        signUpButton.setOnAction(event->{
                String username = signUpUsername.getText();
                String password = signUpPassword.getText();

                if (!password.equals("") || !username.equals(""))
                    createAccount(username, password, event);
                else signUpErrorLabel.setVisible(true);
            });

        signUpUsername.setOnMouseClicked(event -> signUpErrorLabel.setVisible(false));

        backButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/ChooseHowToSignScene.fxml"));
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
        Scene scene2 = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene2);
        window.show();
    }


    /**
     * Calls the create account method in the network. If the clientInfo username is null the player must re-introduce
     * both username and password because he/she could have done a mistake; if it is different from null it calls the
     * next scene.
     *
     * @param username is the string inserted by the player
     * @param password is the password string inserted by the player
     * @param event is the event related to the action done by the user on the create account button
     */
    private void createAccount(String username, String password, ActionEvent event) {
        Thread signUp = new Thread(()->{
            try {
                networkClient.createUser(username, password);
                Platform.runLater(() -> changeSceneHandle(event, "/view/gui/SetNewGameScene.fxml"));
            } catch (CannotRegisterUserException e) {
                Platform.runLater(()->{
                signUpErrorLabel.setText(e.getMessage());
                signUpErrorLabel.setVisible(true);
                signUpUsername.clear();
                signUpPassword.clear();
                });
            }
        });
        signUp.start();
    }
}
