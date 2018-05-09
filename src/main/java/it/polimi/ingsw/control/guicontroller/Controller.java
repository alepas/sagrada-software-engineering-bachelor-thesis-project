package it.polimi.ingsw.control.guicontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;


public class Controller {
    @FXML public Button button1;

    @FXML public Button button2;


    public void initialize() {
        button1.setOnAction(event -> changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/signUp.fxml"));

        button2.setOnAction(event -> changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/signIn.fxml"));
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
}
