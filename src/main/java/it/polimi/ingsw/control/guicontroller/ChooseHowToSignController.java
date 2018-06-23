package it.polimi.ingsw.control.guicontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseHowToSignController {

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;


    @FXML
    void initialize(){

        signUpButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/SignUpScene.fxml"));

        signInButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/SignInScene.fxml"));
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
