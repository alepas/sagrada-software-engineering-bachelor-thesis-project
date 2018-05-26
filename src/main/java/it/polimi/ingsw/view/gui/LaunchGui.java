package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class LaunchGui extends Application {

        public static void main(String[] args) {
            Application.launch(LaunchGui.class, null);
        }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/view/gui/guiview/StartingScene.fxml"));
        stage.setTitle("Sagrada");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

    }
}
