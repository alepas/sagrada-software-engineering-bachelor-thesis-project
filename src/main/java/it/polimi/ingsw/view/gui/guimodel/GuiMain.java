package it.polimi.ingsw.view.gui.guimodel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {

    public static void main(String[] args) { Application.launch(GuiMain.class, args); }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/view/gui/guiview/gui.fxml"));
        stage.setTitle("Sagrada");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
