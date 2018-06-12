package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class LaunchGui extends Application {

    public static void main() {
            Application.launch(LaunchGui.class, (String) null);
        }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/view/gui/guiview/StartingScene.fxml"));
        stage.setTitle("Sagrada");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        final java.net.URL resource = getClass().getResource("song.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        stage.show();

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

    }
}
