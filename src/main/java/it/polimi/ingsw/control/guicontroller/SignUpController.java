package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import javafx.application.Platform;
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

import javafx.event.ActionEvent;

import java.io.IOException;

/*JOptionPane errorlabel = new JOptionPane();
  errorlabel.setFont( new java.awt.Font("Ariel", 0, 12));
  errorlabel.showMessageDialog(null, "Error", "ERRORE",  JOptionPane.ERROR_MESSAGE);*/

public class SignUpController {
    private NetworkClient networkClient;
    private ClientModel clientModel;

    @FXML private PasswordField signUpPassword;

    @FXML private Button signUpButton;

    @FXML private TextField signUpUsername;

    @FXML private Label signUpErrorLabel;

    public void  initialize(){
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        signUpButton.setOnAction(event->{
                String username = signUpUsername.getText();
                String password = signUpPassword.getText();

                if (!password.equals("") && !username.equals("")) {
                    createAccount(username, password);

                    if (clientModel.getUsername() == null) {
                        signUpErrorLabel.setVisible(true);
                        signUpUsername.clear();
                        signUpPassword.clear();
                    } else changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/entranceTotheGame.fxml");

                } else signUpErrorLabel.setVisible(true);

            });

        signUpUsername.setOnMouseClicked(event -> signUpErrorLabel.setVisible(false));
    }


    private void changeSceneHendle(ActionEvent event, String path) {
        AnchorPane nextNode = null;
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


    private void createAccount(String username, String password) {

            Platform.runLater(() -> {
                try {
                    clientModel.setUsername(username);
                    networkClient.createUser(username, password);
                } catch (CannotRegisterUserException e) {
                    clientModel.setUsername(null);
                    e.printStackTrace();
                }
            });
        }
}
