package client.controller.guicontroller;



import client.network.ClientInfo;
import client.network.NetworkClient;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

import java.io.IOException;

public class PersonalAreaController {
    private NetworkClient networkClient;
    private ClientInfo clientInfo;


    @FXML
    private Label personalAreaUsername;

    @FXML
    private Label personalAreaRanking;

    @FXML
    private Label personalAreaWon;

    @FXML
    private Label personalAreaLost;

    @FXML
    private Label personalAreaScore;

    @FXML
    private Button newGameButton;

    public void initialize() {
        networkClient = NetworkClient.getInstance();
        clientInfo = ClientInfo.getInstance();
        try {
            networkClient.getUserStat(clientInfo.getUserToken());
            personalAreaUsername.setText(clientInfo.getUser().getUsername());
            personalAreaRanking.setText(String.valueOf(clientInfo.getUser().getRanking()));
            personalAreaWon.setText(String.valueOf(clientInfo.getUser().getWonGames()));
            personalAreaLost.setText(String.valueOf(clientInfo.getUser().getLostGames()));
            personalAreaScore.setText(String.valueOf(clientInfo.getUser().getAbandonedGames()));
        } catch (CannotFindUserInDBException e) {
            System.out.println("opensuse");
        }

        newGameButton.setOnAction(event -> changeSceneHandle(event, "/client/view/gui/fxml/SetNewGameScene.fxml"));
    }


    private void changeSceneHandle(Event event, String path) {

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
