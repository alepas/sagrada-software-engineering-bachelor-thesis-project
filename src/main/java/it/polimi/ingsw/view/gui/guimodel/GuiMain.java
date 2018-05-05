package it.polimi.ingsw.view.gui.guimodel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {
        Stage window;
        public static void main(String[] args) { Application.launch(GuiMain.class, args); }

        @Override
        public void start(Stage stage) throws IOException {
            window = stage;
            Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/view/gui/guiview/gui.fxml"));
            window.setTitle("Sagrada");
            window.setScene(new Scene(root, 900, 700));
            window.show();
        }

}
