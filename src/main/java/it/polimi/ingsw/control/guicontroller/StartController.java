package it.polimi.ingsw.control.guicontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;


public class StartController {
    @FXML public Button button1;

    @FXML public Button button2;


    public void initialize() {
        button1.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SignUpScene.fxml"));

        button2.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SignInScene.fxml"));
    }

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
