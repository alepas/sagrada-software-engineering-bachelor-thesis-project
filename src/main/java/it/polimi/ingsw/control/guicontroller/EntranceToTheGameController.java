package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class EntranceToTheGameController {

    private NetworkClient networkClient;
    private ClientModel clientModel;

    @FXML private Button createGameButton;

    @FXML private Button personalAreaButton;

    @FXML private Label errorLabel;

    @FXML private Label findPlayersLabel;

    @FXML private RadioButton twoPlayersBox;

    @FXML private RadioButton soloPlayerBox;

    @FXML private RadioButton threePlayersBox;

    @FXML private RadioButton fourPlayersBox;

    @FXML
    void initialize() {

        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        ToggleGroup group = new ToggleGroup();
        soloPlayerBox.setToggleGroup(group);
        twoPlayersBox.setToggleGroup(group);
        threePlayersBox.setToggleGroup(group);
        fourPlayersBox.setToggleGroup(group);

        createGameButton.setOnAction(event -> {

            if( soloPlayerBox.isSelected())
                changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml", "1");

            else if( twoPlayersBox.isSelected()) {
                findGame(2);
                System.out.println(clientModel.getGameID());
                if(clientModel.getGameID()!= null){
                    playGame();
                    changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml", "2");
                }

            }
            else if( threePlayersBox.isSelected()) {
                findGame(3);
                changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml", "3");
            }
            else if( fourPlayersBox.isSelected()){
                findPlayersLabel.setVisible(true);
                findGame(4);
                changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml", "4");
            }
            else
                errorLabel.setText("Select a number.");
        });

        soloPlayerBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        twoPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        threePlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        fourPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        personalAreaButton.setOnAction(event -> personalAreaHendle(event, "/it/polimi/ingsw/view/gui/guiview/userArea.fxml"));
    }



    private void findGame(int numPlayers) {
        Platform.runLater(()->{
            try {
                try {
                    System.out.println(clientModel.getUserToken());
                    networkClient.findGame(clientModel.getUserToken(), numPlayers);
                } catch (InvalidPlayersException e) {
                //TODO
                    e.printStackTrace();
                } catch (NullTokenException e) {
                //TODO
                    e.printStackTrace();
                } catch (CannotFindUserInDBException e) {
                    //TODO
                    e.printStackTrace();
                }
            } catch (Exception e) {
                errorLabel.setText("Impossible to find a game. Try later.");
                errorLabel.setVisible(true);
                e.printStackTrace();
            }
        });
    }


    private void playGame() {
        findPlayersLabel.setVisible(true);
        Thread waitingPlayers = new Thread(
                () -> {
                    do {}
                    while (true);
                }
        );

        waitingPlayers.start();

        try {
            waitingPlayers.join();
        } catch (InterruptedException e){
            errorLabel.setText("I'm not wating anymore.");
            errorLabel.setVisible(true);
        }

    }


    private void changeSceneHendle(ActionEvent event, String path, String numplayers) {
        AnchorPane nextNode = null;
        try {
            nextNode = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        Label numPlayers = new Label(numplayers);
        numPlayers.setVisible(false);
        numPlayers.setId("numPayers");
        window.show();
    }


    private void personalAreaHendle(ActionEvent event, String path) {
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
