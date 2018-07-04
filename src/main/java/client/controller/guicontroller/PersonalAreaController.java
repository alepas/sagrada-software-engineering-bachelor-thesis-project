package client.controller.guicontroller;



import client.network.ClientInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import shared.clientInfo.ClientUser;

import java.io.IOException;

public class PersonalAreaController {


    @FXML private Label personalAreaUsername;

    @FXML private Label personalAreaRanking;

    @FXML private Label personalAreaWon;

    @FXML private Label personalAreaLost;

    @FXML private Label personalAreaScore;

    @FXML private Button newGameButton;


    /**
     * sets all texts and contains the lambda related to the only button in this scene
     */
    public void initialize(){
        ClientInfo clientInfo = ClientInfo.getInstance();
        ClientUser clientUser = clientInfo.getUser();

        personalAreaUsername.setText(clientInfo.getUsername());
        personalAreaRanking.setText(String.valueOf(clientUser.getRanking()));
        personalAreaWon.setText(String.valueOf(clientUser.getWonGames()));
        personalAreaLost.setText(String.valueOf(clientUser.getLostGames()));
        personalAreaScore.setText(String.valueOf(clientUser.getAbandonedGames()));

        newGameButton.setOnAction(event -> changeSceneHandle(event, "/client/view/gui/fxml/SetNewGameScene.fxml"));
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
            Thread.currentThread().interrupt();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
