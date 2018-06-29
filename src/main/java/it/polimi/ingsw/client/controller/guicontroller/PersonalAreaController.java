package it.polimi.ingsw.client.controller.guicontroller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PersonalAreaController {


    @FXML private Label personalAreaUsername;

    @FXML private Label personalAreaRanking;

    @FXML private Label personalAreaWon;

    @FXML private Label personalAreaLost;

    @FXML private Label personalAreaScore;

    @FXML private Button newGameButton;

    public void initialize(){

        //personalAreaUsername.setText(clientUser.getUsername());
        /*personalAreaRanking.setText(String.valueOf(clientUser.getRanking()));
        personalAreaWon.setText(String.valueOf(clientUser.getWonGames()));
        personalAreaLost.setText(String.valueOf(clientUser.getLostGames()));
        personalAreaScore.setText(String.valueOf(clientUser.getAbandonedGames()));*/

        newGameButton.setOnAction(event -> changeSceneHandle(event, "/view/gui/SetNewGameScene.fxml"));
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
