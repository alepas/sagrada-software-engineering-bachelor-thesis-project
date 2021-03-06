package client.controller.guicontroller;

import client.network.ClientInfo;
import client.network.NetworkClient;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import shared.clientinfo.ClientUser;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

import java.io.IOException;

public class PersonalAreaController {


    @FXML
    private Label personalAreaUsername;

    @FXML
    private Label personalAreaRanking;

    @FXML
    private Label personalAreaWon;

    @FXML
    private Label personalAreaLost;

    @FXML
    private Label personalAreaAbandoned;

    @FXML
    private Button newGameButton;

    @FXML
    private Label personalAreaErrorLabel;

    /**
     * sets all texts and contains the lambda related to the only button in this scene
     */
    public void initialize() {
        NetworkClient networkClient = NetworkClient.getInstance();
        ClientInfo clientInfo = ClientInfo.getInstance();
        ClientUser clientUser = clientInfo.getUser();

        try{
            networkClient.getUserStat(clientInfo.getUserToken());
            personalAreaUsername.setText(clientInfo.getUsername());
            personalAreaRanking.setText(String.valueOf(clientUser.getRanking()));
            personalAreaWon.setText(String.valueOf(clientUser.getWonGames()));
            personalAreaLost.setText(String.valueOf(clientUser.getLostGames()));
            personalAreaAbandoned.setText(String.valueOf(clientUser.getRanking()));
        }
        catch (CannotFindUserInDBException e) {
            personalAreaErrorLabel.setText(e.getMessage());
        }

        newGameButton.setOnAction(this::changeSceneHandle);
    }

    /**
     * Changes the scene in the window.
     *
     * @param event the event related to the desire of the player to change scene
     */
    private void changeSceneHandle(Event event) {

        AnchorPane nextNode = new AnchorPane();
        try {
            nextNode = FXMLLoader.load(getClass().getResource("/client/view/gui/fxml/SetNewGameScene.fxml"));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
