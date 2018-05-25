package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;

import it.polimi.ingsw.model.clientModel.*;

import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;

import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class SetNewGameController implements Observer, NotificationHandler {

    private boolean gameStarted = false;
    private boolean areWpcExtracted = false;
    private boolean pocExtracted = false;
    private boolean privateObjExtractd = false;
    private boolean toolExtracted = false;
    private boolean dicesExtracted = false;

    private ClientColor[] colors;
    private ArrayList<ClientWpc> userWpcs = new ArrayList<>();

    private NetworkClient networkClient;
    private ClientModel clientModel;

    private Label lab = new Label();
    private Label lab1 = new Label();
    private Label lab2 = new Label();
    private Label lab3 = new Label();

    private String newPlayer = null;

    @FXML
    private AnchorPane selectionArea;

    @FXML
    private Button createGameButton;

    @FXML
    private Button personalAreaButton;

    @FXML
    private Circle fourthWpcCirlce1;

    @FXML
    private Circle fourthWpcCirlce2;

    @FXML
    private Circle fourthWpcCirlce3;

    @FXML
    private Circle fourthWpcCirlce4;

    @FXML
    private Circle fourthWpcCirlce5;

    @FXML
    private Circle fourthWpcCirlce6;

    @FXML
    private Circle thirdWpcCirlce1;

    @FXML
    private Circle thirdWpcCirlce2;

    @FXML
    private Circle thirdWpcCirlce3;

    @FXML
    private Circle thirdWpcCirlce4;

    @FXML
    private Circle thirdWpcCirlce5;

    @FXML
    private Circle thirdWpcCirlce6;

    @FXML
    private Circle secondWpcCirlce1;

    @FXML
    private Circle secondWpcCirlce2;

    @FXML
    private Circle secondWpcCirlce3;

    @FXML
    private Circle secondWpcCirlce4;

    @FXML
    private Circle secondWpcCirlce5;

    @FXML
    private Circle secondWpcCirlce6;

    @FXML
    private Circle firstWpcCirlce1;

    @FXML
    private Circle firstWpcCirlce2;

    @FXML
    private Circle firstWpcCirlce3;

    @FXML
    private Circle firstWpcCirlce4;

    @FXML
    private Circle firstWpcCirlce5;

    @FXML
    private Circle firstWpcCirlce6;

    @FXML
    private Label errorLabel;

    @FXML
    private Label findPlayersLabel;

    @FXML
    private Label numPlayersInGameLabel;

    @FXML
    private Label numOfPlayersLabel;

    @FXML
    private Label SelectionWpcsLabel;

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

    @FXML
    private Label wpc0Name;

    @FXML
    private Label wpc1Name;

    @FXML
    private Label wpc2Name;

    @FXML
    private Label wpc3Name;

    @FXML
    private GridPane fourthWPC;

    @FXML
    private GridPane firstWPC;

    @FXML
    private GridPane thirdWPC;

    @FXML
    private GridPane secondWPC;



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
                changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SelectWPC.fxml");
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

        personalAreaButton.setOnAction(event -> changeSceneHandle(event,
                "/it/polimi/ingsw/view/gui/guiview/PersonalAreaScene.fxml"));

        firstWPC.setOnMouseClicked(event -> pickWpc(event, wpc0Name.getText()));

        secondWPC.setOnMouseClicked(event -> pickWpc(event, wpc1Name.getText()));

        thirdWPC.setOnMouseClicked(event -> pickWpc(event, wpc2Name.getText()));

        fourthWPC.setOnMouseClicked(event -> pickWpc(event, wpc3Name.getText()));
    }


    /**
     * This method set few javafx elements invisible
     */
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
                SettingOfGame();
            }
        });
    }


    private void SettingOfGame() {
        waitPlayers();
        waitWPC();
        waitForToolCards();
        waitForPoc();
    }


    private void waitPlayers() {
        Thread waitPlayers = new Thread(() -> {

            if (!(clientModel.getGameActualPlayers() == clientModel.getGameNumPlayers())) {
                findPlayersLabel.setVisible(false);
                numOfPlayersLabel.setVisible(true);
            }

            while (!gameStarted) {
                if (newPlayer != null) {

                    Platform.runLater(() -> {
                        lab2.setText(" é entrato in partita " + newPlayer);
                        lab2.setLayoutX(100);
                        lab2.setLayoutY(360);
                        lab1.setVisible(false);
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
                //TODO
            }
            while (!areWpcExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            Platform.runLater(() -> {
                selectionArea.setVisible(false);
                for (int i = 0; i < userWpcs.size(); i++) {
                    ClientWpc wpc = userWpcs.get(i);
                    switch (i) {
                        case 0:
                            wpc0Name.setText(wpc.getWpcID());
                            setWpc(firstWPC, wpc);
                            setFirstWpcFavours(wpc.getFavours());
                            break;

                        case 1:
                            wpc1Name.setText(wpc.getWpcID());
                            setWpc(secondWPC, wpc);
                            setSecondWpcFavours(wpc.getFavours());
                            break;

                        case 2:
                            wpc2Name.setText(wpc.getWpcID());
                            setWpc(thirdWPC, wpc);
                            setThirdWpcFavours(wpc.getFavours());
                            break;

                        case 3:
                            wpc3Name.setText(wpc.getWpcID());
                            setWpc(fourthWPC, wpc);
                            setFourthWpcFavours(wpc.getFavours());
                            break;
                    }
                }
                SelectionWpcsLabel.setVisible(true);
                firstWPC.setVisible(true);
                secondWPC.setVisible(true);
                thirdWPC.setVisible(true);
                fourthWPC.setVisible(true);
            });
        });
        waitingWPC.start();
    }


    private void waitForPoc() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //TODO
        }
        Thread waitingPoc = new Thread(() -> {
            if (!pocExtracted) System.out.println("In attesa degli obbiettivi pubblici...");
            while (!pocExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
        });

        waitingPoc.start();
    }


    private void waitForToolCards() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //TODO
        }
        Thread waitingToolcards = new Thread(() -> {
            if (!toolExtracted) System.out.println("In attesa delle toolcard...");
            while (!toolExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
        });
        waitingToolcards.start();
    }


    private void setFourthWpcFavours(int favours) {
        switch (favours) {
            case 3:
                fourthWpcCirlce1.setVisible(true);
                fourthWpcCirlce2.setVisible(true);
                fourthWpcCirlce3.setVisible(true);
                fourthWpcCirlce4.setVisible(true);
                break;
            case 4:
                fourthWpcCirlce1.setVisible(true);
                fourthWpcCirlce2.setVisible(true);
                fourthWpcCirlce3.setVisible(true);
                fourthWpcCirlce4.setVisible(true);
                break;
            case 5:
                fourthWpcCirlce1.setVisible(true);
                fourthWpcCirlce2.setVisible(true);
                fourthWpcCirlce3.setVisible(true);
                fourthWpcCirlce4.setVisible(true);
                fourthWpcCirlce5.setVisible(true);
                break;
            case 6:
                fourthWpcCirlce1.setVisible(true);
                fourthWpcCirlce2.setVisible(true);
                fourthWpcCirlce3.setVisible(true);
                fourthWpcCirlce4.setVisible(true);
                fourthWpcCirlce5.setVisible(true);
                fourthWpcCirlce6.setVisible(true);
                break;
        }
    }


    private void setThirdWpcFavours(int favours) {
        switch (favours) {
            case 3:
                thirdWpcCirlce1.setVisible(true);
                thirdWpcCirlce2.setVisible(true);
                thirdWpcCirlce3.setVisible(true);
                break;
            case 4:
                thirdWpcCirlce1.setVisible(true);
                thirdWpcCirlce2.setVisible(true);
                thirdWpcCirlce3.setVisible(true);
                thirdWpcCirlce4.setVisible(true);
                break;
            case 5:
                thirdWpcCirlce1.setVisible(true);
                thirdWpcCirlce2.setVisible(true);
                thirdWpcCirlce3.setVisible(true);
                thirdWpcCirlce4.setVisible(true);
                thirdWpcCirlce5.setVisible(true);
                break;
            case 6:
                thirdWpcCirlce1.setVisible(true);
                thirdWpcCirlce2.setVisible(true);
                thirdWpcCirlce3.setVisible(true);
                thirdWpcCirlce4.setVisible(true);
                thirdWpcCirlce5.setVisible(true);
                thirdWpcCirlce6.setVisible(true);
                break;
        }
    }


    private void setSecondWpcFavours(int favours) {
        switch (favours) {
            case 3:
                secondWpcCirlce1.setVisible(true);
                secondWpcCirlce2.setVisible(true);
                secondWpcCirlce3.setVisible(true);
                break;
            case 4:
                secondWpcCirlce1.setVisible(true);
                secondWpcCirlce2.setVisible(true);
                secondWpcCirlce3.setVisible(true);
                secondWpcCirlce4.setVisible(true);
                break;
            case 5:
                secondWpcCirlce1.setVisible(true);
                secondWpcCirlce2.setVisible(true);
                secondWpcCirlce3.setVisible(true);
                secondWpcCirlce4.setVisible(true);
                secondWpcCirlce5.setVisible(true);
                break;
            case 6:
                secondWpcCirlce1.setVisible(true);
                secondWpcCirlce2.setVisible(true);
                secondWpcCirlce3.setVisible(true);
                secondWpcCirlce4.setVisible(true);
                secondWpcCirlce5.setVisible(true);
                secondWpcCirlce6.setVisible(true);
                break;
        }
    }


    private void setFirstWpcFavours(int favours) {
        switch (favours) {
            case 3:
                firstWpcCirlce1.setVisible(true);
                firstWpcCirlce2.setVisible(true);
                firstWpcCirlce3.setVisible(true);
                break;
            case 4:
                firstWpcCirlce1.setVisible(true);
                firstWpcCirlce2.setVisible(true);
                firstWpcCirlce3.setVisible(true);
                firstWpcCirlce4.setVisible(true);
                break;
            case 5:
                firstWpcCirlce1.setVisible(true);
                firstWpcCirlce2.setVisible(true);
                firstWpcCirlce3.setVisible(true);
                firstWpcCirlce4.setVisible(true);
                firstWpcCirlce5.setVisible(true);
                break;
            case 6:
                firstWpcCirlce1.setVisible(true);
                firstWpcCirlce2.setVisible(true);
                firstWpcCirlce3.setVisible(true);
                firstWpcCirlce4.setVisible(true);
                firstWpcCirlce5.setVisible(true);
                firstWpcCirlce6.setVisible(true);
                break;
        }
    }


    /**
     * @param gridPane  //todo
     * @param wpc  //todo
     */
    private void setWpc(GridPane gridPane, ClientWpc wpc) {

        for (ClientCell cell : wpc.getSchema()) {
            int row = cell.getCellPosition().getRow();
            int column = cell.getCellPosition().getColumn();

            AnchorPane cellXY = new AnchorPane();
            gridPane.add(cellXY, column, row);

            ClientColor color = cell.getCellColor();
            fillColor(cellXY, color);

            int number = cell.getCellNumber();
            fillNumber(cellXY, number);
        }
    }


    /**
     * @param cell //todo
     * @param number is the number restriction
     */
    private void fillNumber(AnchorPane cell, int number) {
        switch (number) {
            case 0:
                cell.getStyleClass().add("white");
                break;
            case 1:
                cell.getStyleClass().add("num1");
                break;
            case 2:
                cell.getStyleClass().add("num2");
                break;
            case 3:
                cell.getStyleClass().add("num3");
                break;
            case 4:
                cell.getStyleClass().add("num4");
                break;
            case 5:
                cell.getStyleClass().add("num5");
                break;
            case 6:
                cell.getStyleClass().add("num6");
                break;
        }
    }


    /**
     * @param cell is a cell
     * @param color is the filling color of a schema's cell
     */
    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null) {
            switch (color) {
                case YELLOW:
                    cell.getStyleClass().add("yellow");
                    break;
                case GREEN:
                    cell.getStyleClass().add("green");
                    break;
                case RED:
                    cell.getStyleClass().add("red");
                    break;
                case BLUE:
                    cell.getStyleClass().add("blue");
                    break;
                case VIOLET:
                    cell.getStyleClass().add("violet");
                    break;
            }
        }
        else cell.getStyleClass().add("white");
    }


    /**
     * @param event  //todo
     * @param wpcID  //todo
     */
    public void pickWpc(Event event, String wpcID) {
        Platform.runLater(()-> {
            try {
                networkClient.pickWpc(clientModel.getUserToken(), wpcID);
            } catch (NotYourWpcException e) {
                //TODO
            } catch (CannotFindPlayerInDatabaseException e) {
                e.printStackTrace();
                //TODO
            }
            Label lab4 = new Label("La tua WPC è"+wpcID);
            lab4.setLayoutY(400);
            lab4.setLayoutX(100);
            firstWPC.setDisable(true);
            secondWPC.setDisable(true);
            thirdWPC.setDisable(true);
            fourthWPC.setDisable(true);
        });
        playGame(event);
    }


    private void playGame(Event event) {
        Thread startGame = new Thread(() -> {
            while (!pocExtracted || !toolExtracted || !(clientModel.getWpcByUsername().size() == clientModel.getGameNumPlayers())) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            Platform.runLater(()->{
                switch (clientModel.getGameNumPlayers()){
                    case 1:
                        changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SoloPlayerGameScene.fxml");
                        break;
                    case 2:
                        changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/TwoPlayersGameScene.fxml");
                        break;
                    case 3:
                        changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/ThreePlayersGameScene.fxml");
                        break;
                    case 4:
                        changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/FourPlayersGameScene.fxml");
                        break;
                }
            });
        });
        startGame.start();
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
        userWpcs = notification.wpcsByUser.get(clientModel.getUsername());
        areWpcExtracted = true;
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        colors = notification.colorsByUser.get(clientModel.getUsername());
        privateObjExtractd = true;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        System.out.println(notification.username + " ha scelto la wpc: " + notification.wpc);
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) { toolExtracted = true; }

    @Override
    public void handle(PocsExtractedNotification notification) { pocExtracted = true; }

    @Override
    public void handle(NewRoundNotification notification) {
        dicesExtracted = true;
        System.out.println("Round: " + notification.roundNumber);
        System.out.println("Dadi estratti: ");
        for (ClientDice dice : notification.extractedDices){
            System.out.println("ID: " + dice.getDiceID() + "\t" + dice.getDiceNumber() + " " + dice.getDiceColor());
        }
    }

    @Override
    public void handle(NextTurnNotification notification) {
        System.out.println("Turno: " + notification.turnNumber + "\tRound: " + clientModel.getCurrentRound());
        System.out.println("Turno di " + notification.activeUser);
    }

    @Override
    public void handle(DiceChangedNotification notification) {

    }

    @Override
    public void handle(DicePlacedNotification notification) {

    }

    @Override
    public void handle(ToolCardCanceledNotification notification) {

    }

    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

}
