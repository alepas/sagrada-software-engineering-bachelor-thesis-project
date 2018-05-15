package it.polimi.ingsw.control.guicontroller;



import it.polimi.ingsw.model.clientModel.ClientModel;
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

public class UserArea {

    private ClientModel clientModel;

    @FXML private Label personalAreaUsername;

    @FXML private Label personalAreaToken;

    @FXML private Label personalAreaWon;

    @FXML private Label personalAreaLost;

    @FXML private Label personalAreaScore;

    @FXML private Button newGameButton;

    public void initialize(){

        clientModel = ClientModel.getInstance();

        personalAreaUsername.setText(clientModel.getUsername());
        personalAreaToken.setText(clientModel.getUserToken());
        personalAreaWon.setText("0");
        personalAreaLost.setText("0");
        personalAreaScore.setText("0");

        newGameButton.setOnAction(event -> changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/entrancetothegame.fxml"));
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
