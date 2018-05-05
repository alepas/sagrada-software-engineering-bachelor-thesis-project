package it.polimi.ingsw.view.gui.guicontroller;



import javafx.fxml.FXML;
import it.polimi.ingsw.control.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;


public class SignUpController {
    private ClientController controller;


    @FXML private PasswordField signUpPassword;

    @FXML private Button signUpButton;

    @FXML private TextField signUpUsername;


    public void  initialize(){

        signUpButton.setOnAction(event->{
            createAccount();
            changeSceneHendle(event ,"/it/polimi/ingsw/view/gui/guiview/signIn.fxml");
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


    private void createAccount() {

        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        System.out.println(password);
        System.out.println(username);

        if(!username.equals("") || !password.equals("")) {
            controller.createUser(username, password);
        }
        else
            System.out.println("Error login user");
    }

}
