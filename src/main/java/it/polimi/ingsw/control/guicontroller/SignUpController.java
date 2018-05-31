package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
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

/*JOptionPane errorlabel = new JOptionPane();
  errorlabel.setFont( new java.awt.Font("Ariel", 0, 12));
  errorlabel.showMessageDialog(null, "Error", "ERRORE",  JOptionPane.ERROR_MESSAGE);*/

public class SignUpController {

    private NetworkClient networkClient;
    private ClientModel clientModel;

    @FXML private PasswordField signUpPassword;

    @FXML private Button backButton;

    @FXML private Button signUpButton;

    @FXML private TextField signUpUsername;

    @FXML private Label signUpErrorLabel;

    public void  initialize(){
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        signUpButton.setOnAction(event->{
                String username = signUpUsername.getText();
                String password = signUpPassword.getText();

                if (!password.equals("") || !username.equals(""))
                    createAccount(username, password, event);
                else signUpErrorLabel.setVisible(true);
            });

        signUpUsername.setOnMouseClicked(event -> signUpErrorLabel.setVisible(false));

        backButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/StartingScene.fxml"));
    }


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


    private void createAccount(String username, String password, ActionEvent event) {
        Thread signUp = new Thread(()->{
            try {
                networkClient.createUser(username, password);
            } catch (CannotRegisterUserException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //TODO
            }

            Platform.runLater(() -> {
                if (clientModel.getUsername() == null) {
                    signUpErrorLabel.setVisible(true);
                    signUpUsername.clear();
                    signUpPassword.clear();
                }
                else changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SetNewGameScene.fxml");
            });
        });
        signUp.start();
    }
}
