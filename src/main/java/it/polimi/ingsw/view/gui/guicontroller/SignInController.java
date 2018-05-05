package it.polimi.ingsw.view.gui.guicontroller;

import it.polimi.ingsw.control.ClientController;
import it.polimi.ingsw.control.network.NetworkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class SignInController {
    private NetworkClient client;

        @FXML private TextField signInUsername;

        @FXML private PasswordField signInPassword;

        @FXML private Button signInButton;

    //private final ClientController controller;

    /*public SignInController(ClientController controller) {
        this.controller = controller;
    }*/

    public void initialize( ) {
        signInButton.setOnAction(event->{
            loginUser();
            changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/entranceTotheGame.fxml");
        });
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

    public void loginUser() {

        //ClientController clientController = new ClientController(client);
        String username = signInUsername.getText();
        String password = signInPassword.getText();
        if(!signInUsername.equals("") || !signInPassword.equals("")) {
            //controller.login(username, password);
            System.out.println("login effettuato");
        }
        else
            System.out.println("Error login user");
    }

}
