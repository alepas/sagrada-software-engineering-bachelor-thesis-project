package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;

import it.polimi.ingsw.model.clientModel.*;

import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;

import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.String.valueOf;

public class SetNewGameController implements Observer, NotificationHandler {

    private final Object playerWaiter = new Object();
    private final Object cardWaiter = new Object();
    private final Object wpcWaiter = new Object();

    private boolean gameStarted = false;
    private boolean areWpcExtracted = false;
    private boolean pocExtracted = false;
    private boolean privateObjExtractd = false;
    private boolean toolExtracted = false;
    private boolean playerIn = false;
    private boolean playerOut = false;

    private ClientColor[] colors;
    private ArrayList<ClientWpc> userWpcs = new ArrayList<>();

    private NetworkClient networkClient;
    private ClientModel clientModel;

    private Label lab = new Label();
    private Label lab1 = new Label();
    private Label lab2 = new Label();
    private Label lab3 = new Label();

    private String newPlayer = null;

    @FXML private AnchorPane privateObjArea;

    @FXML
    private AnchorPane selectionArea;

    @FXML
    private Button createGameButton;

    @FXML
    private Button personalAreaButton;

    @FXML
    private Circle fourthWpcCircle4;

    @FXML
    private Circle fourthWpcCircle5;

    @FXML
    private Circle fourthWpcCircle6;

    @FXML
    private Circle thirdWpcCircle4;

    @FXML
    private Circle thirdWpcCircle5;

    @FXML
    private Circle thirdWpcCircle6;

    @FXML
    private Circle secondWpcCircle4;

    @FXML
    private Circle secondWpcCircle5;

    @FXML
    private Circle secondWpcCircle6;

    @FXML
    private Circle firstWpcCircle4;

    @FXML
    private Circle firstWpcCircle5;

    @FXML
    private Circle firstWpcCircle6;

    @FXML
    private Label errorLabel;

    @FXML
    private Label findPlayersLabel;

    @FXML
    private Label numPlayersInGameLabel;

    @FXML
    private Label numOfPlayersLabel;

    @FXML
    private Label selectionWpcsLabel;

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
        clientModel.addObserver(this);

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
                Platform.runLater(()->{
                    errorLabel.setText("Select a number.");
                    errorLabel.setVisible(true);
                });
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
                //todo
                e.printStackTrace();
            } catch (CannotCreatePlayerException e) {
                errorLabel.setText(e.getMessage());
                errorLabel.setVisible(true);
                createGameButton.setDisable(false);
                personalAreaButton.setDisable(false);
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

    /**
     * calls the methods that wait all the elements that compose a game
     */
    private void SettingOfGame() {
        waitPlayers();
        waitWPC();
    }


    private void waitPlayers() {
        TranslateTransition transition = new TranslateTransition();
        ImageView image = new ImageView();
        diceAnimation(image, transition);
        Thread waitPlayers = new Thread(() -> {
            if (clientModel.getGameActualPlayers() != clientModel.getGameNumPlayers()) {
                findPlayersLabel.setVisible(false);
                numOfPlayersLabel.setVisible(true);
            }
            while (!gameStarted) {
                synchronized (playerWaiter){
                    try {
                        while (newPlayer== null)  playerWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> {
                        if(playerIn) {
                            lab2.setText(newPlayer + " é entrato in partita!");
                            lab2.setLayoutX(100);
                            lab2.setLayoutY(360);
                            selectionArea.getChildren().add(lab2);
                            playerIn = false;
                        }
                        if(playerOut){
                            lab3.setText(newPlayer + " é uscito dalla partita!");
                            lab3.setLayoutX(100);
                            lab3.setLayoutY(340);
                            selectionArea.getChildren().add(lab3);
                            playerOut= false;
                        }
                        newPlayer = null;
                    });

            }
            Platform.runLater(()->{
                transition.stop();
                selectionArea.getChildren().remove(image);
            });
        });
        waitPlayers.start();
    }


    private void diceAnimation(ImageView image, TranslateTransition transition) {
        Platform.runLater(()->{
            image.getStyleClass().add("violet5");
            image.setFitHeight(30);
            image.setFitWidth(30);
            selectionArea.getChildren().add(image);
            image.setLayoutX(100);
            image.setLayoutY(450);

            transition.setDuration(Duration.seconds(4));
            transition.setToX(300);
            transition.setAutoReverse(true);
            transition.setCycleCount(Animation.INDEFINITE);
            transition.setNode(image);
            transition.play();
        });
    }


    private void waitWPC() {
        Thread waitingWPC = new Thread(() -> {
            while (!areWpcExtracted) {
                synchronized (wpcWaiter) {
                    try {
                        wpcWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                            default:
                                break;
                        }
                    }
                    setVisibleWpcSelection();
                });

        });
        waitingWPC.start();
    }

    /**
     * Sets all favafx elements related to the selection of the user's personal schema visible.
     */
    private void setVisibleWpcSelection(){
        Platform.runLater(()->{
            selectionWpcsLabel.setVisible(true);
            firstWPC.setVisible(true);
            secondWPC.setVisible(true);
            thirdWPC.setVisible(true);
            fourthWPC.setVisible(true);
            for (ClientColor color : colors) {
                String style = "prOC".concat(String.valueOf(color).toLowerCase());
                privateObjArea.setId(style);
            }
        });
    }


    private void setFourthWpcFavours(int favours) {
        switch (favours) {
            case 3:
                fourthWpcCircle6.setVisible(false);
                fourthWpcCircle5.setVisible(false);
                fourthWpcCircle4.setVisible(false);
                break;
            case 4:
                fourthWpcCircle6.setVisible(false);
                fourthWpcCircle5.setVisible(false);
                break;
            case 5:
                fourthWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }


    private void setThirdWpcFavours(int favours) {
        switch (favours) {
            case 3:
                thirdWpcCircle6.setVisible(false);
                thirdWpcCircle5.setVisible(false);
                thirdWpcCircle4.setVisible(false);
                break;
            case 4:
                thirdWpcCircle6.setVisible(false);
                thirdWpcCircle5.setVisible(false);
                break;
            case 5:
                thirdWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }


    private void setSecondWpcFavours(int favours) {
        switch (favours) {
            case 3:
                secondWpcCircle6.setVisible(false);
                secondWpcCircle5.setVisible(false);
                secondWpcCircle4.setVisible(false);
                break;
            case 4:
                secondWpcCircle6.setVisible(false);
                secondWpcCircle5.setVisible(false);
                break;
            case 5:
                secondWpcCircle6.setVisible(false);
                break;
            default:
                break;
        }
    }


    private void setFirstWpcFavours(int favours) {
        switch (favours) {
            case 3:
                firstWpcCircle6.setVisible(false);
                firstWpcCircle5.setVisible(false);
                firstWpcCircle4.setVisible(false);
                break;
            case 4:
                firstWpcCircle6.setVisible(false);
                firstWpcCircle5.setVisible(false);
                break;
            case 5:
                firstWpcCircle6.setVisible(false);
                break;
            default:
                break;

        }
    }


    /**
     * Fills the grid with AnchorPanes filled with restrictions by calling the two methods fillNumber and fillColor.
     *
     * @param gridPane is the schema tat will be fill
     * @param wpc  is the schema which is going to tell how to fill the gridPane
     */
    private void setWpc(GridPane gridPane, ClientWpc wpc) {
        Platform.runLater(()-> privateObjArea.setVisible(true));
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
     * @param cell is the anchorPane that i have to fill
     * @param number is the number restriction
     */
    private void fillNumber(AnchorPane cell, int number) {
        switch (number) {
            case 0:
                cell.getStyleClass().add("white");
                break;
            default:
                String style = "num" + valueOf(number);
                cell.getStyleClass().add(style);
                break;
        }
    }


    /**
     * @param cell is a cell
     * @param color is the filling color of a schema's cell
     */
    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null)
            cell.getStyleClass().add(color.toString().toLowerCase());
        else cell.getStyleClass().add("white");
    }


    /**
     * Calls the network method that will assign the schema chosen to the player.
     *
     * @param event  is the event generated by the action of the user while he/she has chosen the wpc
     * @param wpcID  is the ID of the schema chosen by the player
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
            firstWPC.setDisable(true);
            secondWPC.setDisable(true);
            thirdWPC.setDisable(true);
            fourthWPC.setDisable(true);
        });
        playGame(event);
    }


    private void playGame(Event event) {
        Thread startGame = new Thread(() -> {
            while (!pocExtracted || !toolExtracted || (clientModel.getGameNumPlayers() != clientModel.getWpcByUsername().size()) || !privateObjExtractd) {
                synchronized (cardWaiter){
                    try {
                        cardWaiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
            playerIn = true;
            newPlayer = notification.username;
        } else {
            playerOut = true;
            newPlayer = notification.username;
        }
        synchronized (playerWaiter){ playerWaiter.notify(); }
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        userWpcs = notification.wpcsByUser.get(clientModel.getUsername());
        areWpcExtracted = true;
        synchronized (wpcWaiter){ wpcWaiter.notify();}
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        colors = notification.colorsByUser.get(clientModel.getUsername());
        privateObjExtractd = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {}

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        toolExtracted = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        pocExtracted = true;
        synchronized (cardWaiter){cardWaiter.notify();}
    }

    @Override
    public void handle(NewRoundNotification notification) {
    }

    @Override
    public void handle(NextTurnNotification notification) {
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
    }

    @Override
    public void handle(DicePlacedNotification notification) {

    }


    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

    @Override
    public void handle(ScoreNotification notification) {
    }

    @Override
    public void handle(ToolCardDicePlacedNotification toolCardDicePlacedNotification) {

    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification toolCardExtractedDicesModifiedNotification) {

    }

}
