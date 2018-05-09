package it.polimi.ingsw.control.guicontroller;

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

    @FXML private PasswordField signUpPassword;

    @FXML private Button signUpButton;

    @FXML private TextField signUpUsername;

    @FXML private Label signUpErrorLabel;

    public void  initialize(){

        signUpButton.setOnAction(event->{

            if(createAccount() == null)
                signUpErrorLabel.setVisible(true);    //rendo la label di errore visibile
            else
                changeSceneHendle(event ,"/it/polimi/ingsw/view/gui/guiview/entranceTotheGame.fxml");
        });

        signUpUsername.setOnAction(event -> signUpErrorLabel.setVisible(false));
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


    private String createAccount() {

        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        String user =null;

//        if(!username.equals("") && !password.equals(""))
//             user = controller.createUser(username, password);

        return user;
    }
}
