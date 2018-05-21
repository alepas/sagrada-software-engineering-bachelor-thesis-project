package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.responses.notifications.*;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

import it.polimi.ingsw.model.wpc.Cell;
import it.polimi.ingsw.model.wpc.WPC;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class SetNewGameController implements Observer, NotificationHandler {

    @FXML
    private AnchorPane selectionArea;
    private boolean gameStarted = false;

    private boolean areWpcExtracted = false;
    private ArrayList<String> userWpcs;

    private NetworkClient networkClient;
    private ClientModel clientModel;

    private Label lab = new Label();
    private Label lab1 = new Label();
    private Label lab2 = new Label();
    private String newPlayer = null;

    @FXML
    private Button createGameButton;

    @FXML
    private Button personalAreaButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label findPlayersLabel;

    @FXML
    private Label numPlayersInGameLabel;

    @FXML
    private Label numOfPlayersLabel;

    @FXML
    private Label startGameLabel;

    @FXML
    private RadioButton twoPlayersBox;

    @FXML
    private RadioButton soloPlayerBox;

    @FXML
    private RadioButton threePlayersBox;

    @FXML
    private RadioButton fourPlayersBox;

    @FXML private Label wpc1;

    @FXML private Label wpc2;

    @FXML private Label wpc3;

    @FXML private Label wpc4;

    @FXML private GridPane fourthWPC;

    @FXML private GridPane firstWPC;

    @FXML private GridPane thirdWPC;

    @FXML private GridPane secondWPC;


    @FXML
    void initialize() {

        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();
        clientModel.setObserver(this);

        ToggleGroup group = new ToggleGroup();
        soloPlayerBox.setToggleGroup(group);
        twoPlayersBox.setToggleGroup(group);
        threePlayersBox.setToggleGroup(group);
        fourPlayersBox.setToggleGroup(group);

        createGameButton.setOnAction(event -> {


            if (soloPlayerBox.isSelected()) {
                disableAll();
                changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml");
            } else if (twoPlayersBox.isSelected()) {
                disableAll();
                findGame(2);
            } else if (threePlayersBox.isSelected()) {
                disableAll();
                findGame(3);
            } else if (fourPlayersBox.isSelected()) {
                disableAll();
                findGame(4);
            } else {
                errorLabel.setText("Select a number.");
                errorLabel.setVisible(true);
            }
        });

        soloPlayerBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        twoPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        threePlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        fourPlayersBox.setOnMouseClicked(event -> errorLabel.setVisible(false));

        personalAreaButton.setOnAction(event -> changeSceneHendle(event, "/it/polimi/ingsw/view/gui/guiview/PersonalAreaScene.fxml"));

        firstWPC.setOnMouseClicked(event -> pickWpc(wpc1.getText()));

        secondWPC.setOnMouseClicked(event-> pickWpc(wpc2.getText()));

        thirdWPC.setOnMouseClicked( event-> pickWpc(wpc3.getText()));

        fourthWPC.setOnMouseClicked(event-> pickWpc(wpc4.getText()));
    }

    private void disableAll() {
        numPlayersInGameLabel.setVisible(false);
        createGameButton.setVisible(false);
        personalAreaButton.setVisible(false);
        soloPlayerBox.setVisible(false);
        twoPlayersBox.setVisible(false);
        threePlayersBox.setVisible(false);
        fourPlayersBox.setVisible(false);
        findPlayersLabel.setVisible(true);
    }


    private void findGame(int numPlayers) {
        Platform.runLater(() -> {
            try {
                networkClient.findGame(clientModel.getUserToken(), numPlayers);
            } catch (CannotFindUserInDBException | InvalidNumOfPlayersException e) {
                //TODO
                e.printStackTrace();
            } catch (CannotCreatePlayerException e) {
                errorLabel.setText(e.getMessage());
                errorLabel.setVisible(true);
                createGameButton.setDisable(false);
                personalAreaButton.setDisable(false);
                //TODO
            }

            String gameID = clientModel.getGameID();

            if (gameID != null) {
                startGameLabel.setVisible(false);
                lab.setText("Entrato nella partita: " + gameID);
                lab.setLayoutX(100);
                lab.setLayoutY(300);
                lab1.setText("Giocatori presenti: " + clientModel.getGameActualPlayers() +
                        " di " + clientModel.getGameNumPlayers() + " necessari.");
                lab1.setLayoutX(100);
                lab1.setLayoutY(340);
                selectionArea.getChildren().addAll(lab, lab1);
                playGame();
            }
        });
    }

    private void playGame() {
        waitPlayers();
        waitWPC();
    }


    private void waitPlayers() {
        Thread waitPlayers = new Thread(() -> {

            if (!(clientModel.getGameActualPlayers() == clientModel.getGameNumPlayers())) {
                findPlayersLabel.setVisible(false);
                numOfPlayersLabel.setVisible(true);
            }

            while (!gameStarted) {
                if (newPlayer != null) {
                    System.out.println("ehi");
                    Platform.runLater(()->{
                        lab2.setText(" é entrato in partita " + newPlayer);
                        lab2.setLayoutX(100);
                        lab2.setLayoutY(360);
                        selectionArea.getChildren().add(lab2);
                        newPlayer = null;
                        //TODO: devo fare il caso di uscita da partita e modificare lab1
                    });
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
        });
        waitPlayers.start();
    }

    private void waitWPC() {

        Thread waitingWPC = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            if (!areWpcExtracted) System.out.println("In attesa delle wpc...");
            while (!areWpcExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            Platform.runLater(()->{
                selectionArea.setVisible(false);
                for(int i= 0; i< userWpcs.size(); i++) {
                    switch (i) {
                        case 0:
                            WPC wpc = clientModel.getWpcByID(userWpcs.get(i));
                            wpc1.setText(wpc.getWpcID());
                            for (it.polimi.ingsw.model.wpc.Cell cell : wpc.schema) {
                                int row = cell.getCellPosition().getRow();
                                int column = cell.getCellPosition().getColumn();
                                fillCell(firstWPC, cell, row, column);
                            }
                            break;
                        case 1:
                            WPC wpc1 = clientModel.getWpcByID(userWpcs.get(i));
                            wpc2.setText(wpc1.getWpcID());
                            for (it.polimi.ingsw.model.wpc.Cell cell : wpc1.schema) {
                                int row = cell.getCellPosition().getRow();
                                int column = cell.getCellPosition().getColumn();
                                fillCell(secondWPC, cell, row, column);
                            }
                            break;
                        case 2:
                            WPC wpc2 = clientModel.getWpcByID(userWpcs.get(i));
                            wpc3.setText(wpc2.getWpcID());
                            for (it.polimi.ingsw.model.wpc.Cell cell : wpc2.schema) {
                                int row = cell.getCellPosition().getRow();
                                int column = cell.getCellPosition().getColumn();
                                fillCell(thirdWPC, cell, row, column);
                            }
                            break;
                        case 3:
                            WPC wpc3 = clientModel.getWpcByID(userWpcs.get(i));
                            wpc4.setText(wpc3.getWpcID());
                            for (Cell cell : wpc3.schema) {
                                int row = cell.getCellPosition().getRow();
                                int column = cell.getCellPosition().getColumn();
                                fillCell(fourthWPC, cell, row, column);
                            }
                            break;
                    }
                }
                firstWPC.setVisible(true);
                secondWPC.setVisible(true);
                thirdWPC.setVisible(true);
                fourthWPC.setVisible(true);
            });
        });
        waitingWPC.start();
    }


    private void fillCell(GridPane gridPane, Cell cell, int row, int column) {
        AnchorPane cellXY = new AnchorPane();
        gridPane.add(cellXY, column, row);
        Color color = cell.getCellColor();
        if(color != null) {
            switch (color) {
                case YELLOW:
                    cellXY.setStyle("-fx-background-color: #dfd207;");
                    break;
                case GREEN:
                    cellXY.setStyle("-fx-background-color: #30a35e;");
                    break;
                case RED:
                    cellXY.setStyle("-fx-background-color: #d41d22;");
                    break;
                case BLUE:
                    cellXY.setStyle("-fx-background-color: #5cc7d8;");
                    break;
                case VIOLET:
                    cellXY.setStyle("-fx-background-color: #98258d;");
                    break;
            }
        }
        else
            cellXY.setStyle("-fx-background-color: #ffffff;");
        int number = cell.getCellNumber();
        switch (number) {
            case 1:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos1.png);"+"-fx-background-size: cover");
                break;
            case 2:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos2.png);"+"-fx-background-size: cover");
                break;
            case 3:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos3.png);"+"-fx-background-size: cover");
                break;
            case 4:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos4.png);" +
                        "-fx-background-size: cover");
                break;
            case 5:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos5.png);"+"-fx-background-size: cover");

                break;
            case 6:
                cellXY.setStyle("-fx-background-image: url(view/wpc/pos6.png);"+"-fx-background-size: cover");
                break;
        }
    }

    public void pickWpc(String wpcID) {
        try {
            networkClient.pickWpc(clientModel.getUserToken(), wpcID);
        } catch (NotYourWpcException e) {
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
            //TODO
        }
    }

    private void changeSceneHendle(ActionEvent event, String path) {
            AnchorPane nextNode = null;
            try {
                nextNode = FXMLLoader.load(getClass().getResource(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(nextNode);
            if(areWpcExtracted){
                for(String ID: userWpcs) {
                }
            }
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
    }


    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) { ((Notification) arg).handle(this); }


    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        findPlayersLabel.setVisible(false);
        numOfPlayersLabel.setVisible(false);
        startGameLabel.setVisible(true);
        gameStarted = true;
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        if (notification.joined){
            newPlayer = notification.username;
            System.out.println( "é entrato in partita" + notification.username);
        } else {
            System.out.println(notification.username + " è uscito dalla partita.");
            //TODO
        }
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        userWpcs = notification.wpcsByUser.get(notification.username);
        //clientModel.setWpcIDs(userWpcs);
        System.out.println("Le tue wpc sono:\n\n");
        areWpcExtracted = true;
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        Color[] colors = notification.colorsByUser.get(notification.username);
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (Color color : colors){
            str.append(color + "\t");
        }

        System.out.println(str.toString());
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {}

    @Override
    public void handle(ToolcardsExtractedNotification notification) {

    }

    @Override
    public void handle(PocsExtractedNotification notification) {

    }

}
