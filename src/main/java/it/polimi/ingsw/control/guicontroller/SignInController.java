package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;

import it.polimi.ingsw.model.clientModel.ClientModel;
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

    @FXML private Label signInErrorLabel;


    public void initialize( ) {
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        signInButton.setOnAction(event-> {

                String username = signInUsername.getText();
                String password = signInPassword.getText();

                if (!password.equals("") && !username.equals("")) {

                    loginUser(username, password);

                    if (clientModel.getUsername() == null) {
                        signInErrorLabel.setVisible(true);
                        signInUsername.clear();
                        signInPassword.clear();
                    } else
                        changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SetNewGameScene.fxml");
                } else signInErrorLabel.setVisible(true);

        });

        signInUsername.setOnMouseClicked(event -> signInErrorLabel.setVisible(false));
    }


    private void changeSceneHendle(ActionEvent event, String path) {
        AnchorPane nextNode = null;
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


    private void loginUser(String username, String password) {
        Platform.runLater(()-> {
            try {

                networkClient.login(username, password);
            } catch (CannotLoginUserException e) {

                e.printStackTrace();
                //TODO
            }
        });
    }

}
