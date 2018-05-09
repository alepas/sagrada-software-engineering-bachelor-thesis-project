package it.polimi.ingsw.control.guicontroller;

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

    @FXML private TextField signInUsername;

    @FXML private PasswordField signInPassword;

    @FXML private Button signInButton;

    @FXML private Label signInErrorLabel;

    public void initialize( ) {
        signInButton.setOnAction(event->{
            String username = loginUser();
            if ( username == null)
                signInErrorLabel.setVisible(true);
            else
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
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private String loginUser() {

        String username = signInUsername.getText();
        String password = signInPassword.getText();

//        if(!password.equals("") || !username.equals("")) {
//            return controller.login(username, password);
//        }
//        else
            return null;
    }

}
