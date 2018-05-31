package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class TwoPlayersGameController implements Observer, NotificationHandler {

    @FXML private AnchorPane gameBoard;
    private NetworkClient networkClient;
    private ClientModel clientModel;
    //TODO private ClientColor[] privateObjective;
    private String username;
    private ArrayList<ClientDice> extractedDices = new ArrayList<>();
    private ArrayList<ClientToolCard> toolCardsIDs = new ArrayList<>();
    private ArrayList<ClientPoc> pocIDs = new ArrayList<>();
    private String round;
    private ArrayList<ImageView> dices = new ArrayList<>();
    private ArrayList<AnchorPane> schema = new ArrayList<>();
    private HashMap<String, ClientWpc> wpc;


    @FXML
    private AnchorPane zoomedCard;

    @FXML
    private Button endTurnButton;

    @FXML private Button zoomCardBackButton;

    @FXML private Button zoomCardUseButton;

    @FXML
    private GridPane extractedDicesGrid;

    @FXML
    private GridPane firstWpcGrid;

    @FXML
    private GridPane pocGrid;

    @FXML
    private GridPane privateObjectiveGrid;

    @FXML
    private GridPane secondWpcGrid;

    @FXML
    private GridPane toolCardGrid;

    @FXML
    private Label firstUserLabel;

    @FXML
    private Label roundLabel;

    @FXML
    private Label secondUserLabel;

    @FXML
    private MenuItem toolCardButton;

    @FXML
    private MenuItem roundTrackButton;

    @FXML
    private MenuItem pocButton;

    @FXML
    private MenuItem privateObjectiveButton;

    @FXML
    void initialize() {
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();
        clientModel.setObserver(this);

        //game parameters
        username = clientModel.getUsername();
        toolCardsIDs = clientModel.getGameToolCards();
        pocIDs = clientModel.getGamePublicObjectiveCards();
        extractedDices = clientModel.getExtractedDices();
        wpc = clientModel.getWpcByUsername();
        round = String.valueOf(clientModel.getCurrentRound());

        //set all the elements in the game view
        setToolCard();
        setPoc();
        setDices();
        diceAnimation();
        setWpc();
        waitForTurn();

        toolCardButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            privateObjectiveGrid.setVisible(false);
            toolCardGrid.setVisible(true);
        });

        pocButton.setOnAction(event -> {
            privateObjectiveGrid.setVisible(false);
            toolCardGrid.setVisible(false);
            pocGrid.setVisible(true);
        });

        privateObjectiveButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            toolCardGrid.setVisible(false);
            privateObjectiveGrid.setVisible(true);
        });


        toolCardGrid.setOnMouseClicked(event -> {
            for(Node tool: toolCardGrid.getChildren()) {
                tool.setOnMouseClicked(event1 -> Platform.runLater(() -> {
                    AnchorPane toolCard = new AnchorPane();
                    toolCard.setPrefSize(400, 540);
                    toolCard.setId(tool.getId());

                    zoomedCard.getChildren().add(toolCard);
                    zoomCardBackButton.toFront();
                    zoomCardUseButton.toFront();
                    zoomedCard.setVisible(true);
                }));
            }
        });

        pocGrid.setOnMouseClicked(event -> {
            for(Node poc: pocGrid.getChildren()) {
                poc.setOnMouseClicked(event1 -> Platform.runLater(() -> {
                    AnchorPane toolCard = new AnchorPane();
                    toolCard.setPrefSize(400, 560);
                    toolCard.setId(poc.getId());

                    zoomedCard.getChildren().add(toolCard);
                    zoomCardBackButton.toFront();
                    zoomCardUseButton.toFront();
                    zoomedCard.setVisible(true);
                }));
            }
        });

        zoomCardBackButton.setOnAction(event -> zoomedCard.setVisible(false));

        zoomCardUseButton.setOnAction(event -> System.out.println("uso tool"));


        for (ImageView dice : dices) {
            dice.setOnDragDetected(event -> {
                Dragboard db = dice.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(dice.getImage());
                content.putString(dice.getId());
                dice.setVisible(false);
                db.setContent(content);
                event.consume();
            });

            dice.setOnDragDone(event -> {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    System.out.println("Drag finito");

                } else
                    dice.setVisible(true);
                event.consume();
            });
        }

        for (AnchorPane cell : schema) {
            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell)
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    int id = Integer.parseInt(db.getString());
                    int row = Integer.parseInt(cell.getId().substring(0,1));
                    int column = Integer.parseInt(cell.getId().substring(1,2));
                    Position position = new Position(row, column);
                    placeDice(id, position);
                    for (ClientCell cells : clientModel.getMyWpc().getSchema()) {
                        if (cells.getCellDice() != null && cells.getCellDice().getDiceID() == id) {
                            Image image = db.getImage();
                            ImageView dice = new ImageView(image);
                            dice.setFitWidth(70);
                            dice.setFitHeight(70);
                            cell.getChildren().add(dice);
                            success = true;
                        }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }

        endTurnButton.setOnAction(event -> {
            try {
                System.out.println();
                networkClient.passTurn(clientModel.getUserToken());
                waitForTurn();
            } catch (CannotFindPlayerInDatabaseException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
                e.printStackTrace();
            }

        });
    }

    private void placeDice(int id, Position position) {
        Platform.runLater(()-> {
            try {
                networkClient.placeDice(clientModel.getUserToken(), id, position);
            } catch (CannotFindPlayerInDatabaseException | CannotPickPositionException |
                    CannotPickDiceException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
                e.printStackTrace();
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    private void waitForTurn() {
        Thread waitTurn = new Thread(()->{
            while (!clientModel.isActive()) {
                Platform.runLater(() -> {
                    roundLabel.setText("Round numero " + round + ", in attesa del proprio turno..");
                    extractedDicesGrid.setDisable(true);
                    endTurnButton.setDisable(true);
                });
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            setTurn();
        });
        waitTurn.start();

    }


    private void diceAnimation(){
        for(int row = 0 ; row < extractedDices.size(); row++) {
            ClientDice dice = extractedDices.get(row);

            String color = (String.valueOf(dice.getDiceColor())).toLowerCase();
            String number = String.valueOf(dice.getDiceNumber());
            String id = String.valueOf(dice.getDiceID());
            String style = color.concat(number);

            ImageView image = new ImageView();
            image.getStyleClass().add(style);
            image.setFitHeight(100);
            image.setFitWidth(100);
            dices.add(image);
            image.setId(id);
            gameBoard.getChildren().add(image);
            Path path = new Path();
            path.getElements().add(new MoveTo(0, 0));
            path.getElements().add(new LineTo(extractedDicesGrid.getLayoutX(), extractedDicesGrid.getLayoutY()));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(10000));
            pathTransition.setNode(image);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.NONE);
            pathTransition.play();
            //gameBoard.getChildren().remove(image);
            //extractedDicesGrid.getChildren().get(row).setVisible(true);
        }
    }


    private void setDices( ) {
        for(int row = 0; row < extractedDices.size(); row++) {
            ClientDice dice = extractedDices.get(row);

            String color = (String.valueOf(dice.getDiceColor())).toLowerCase();
            String number = String.valueOf(dice.getDiceNumber());
            String id = String.valueOf(dice.getDiceID());
            String style = color.concat(number);

            ImageView image = new ImageView();
            image.getStyleClass().add(style);
            image.setFitHeight(100);
            image.setFitWidth(100);
            dices.add(image);
            image.setId(id);
            extractedDicesGrid.add(image, 0, row);
        }
    }


    private void setPrivateObjective(ClientColor color, int row) {
        AnchorPane privateObj = new AnchorPane();
        String style = "prOC".concat(String.valueOf(color).toLowerCase());
        privateObj.getStyleClass().add(style);

        privateObjectiveGrid.add(privateObj, 0, row );
        privateObjectiveGrid.setVisible(false);
    }


    private void setPoc() {
        for(int poc = 0; poc < pocIDs.size(); poc++ ) {
            String id = pocIDs.get(poc).getId();
            int finalPoc = poc;
            Platform.runLater(() -> {
                AnchorPane POC = new AnchorPane();
                String style = "poc" + String.valueOf(id);
                POC.setId(style);
                pocGrid.add(POC, 0, finalPoc);
                pocGrid.setVisible(true);
            });
        }
    }


    private void setToolCard( ) {
        for(int tool = 0; tool < toolCardsIDs.size(); tool++) {
            String id = toolCardsIDs.get(tool).getId();
            int finalTool = tool;
            Platform.runLater(() -> {
                AnchorPane toolCard = new AnchorPane();
                String typeCard = "tool";
                String number = String.valueOf(id);
                String style = typeCard.concat(number);

                toolCard.setId(style);
                toolCard.setCursor(Cursor.HAND);
                toolCardGrid.add(toolCard, 0, finalTool);
                toolCardGrid.setVisible(false);
            });
        }
    }


    private void setWpc( ) {
        switch (wpc.size()) {
            case 1:
                firstUserLabel.setText(username);
                fillWpc(firstWpcGrid, wpc.get(username));
                break;
            case 2:
                for (String wpcUser : wpc.keySet()) {
                    if (wpcUser.equals(username)) {
                        firstUserLabel.setText(username);
                        fillWpc(firstWpcGrid, wpc.get(wpcUser));
                    } else {
                        secondUserLabel.setText(wpcUser);
                        fillWpc(secondWpcGrid, wpc.get(wpcUser));
                    }
                }
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }


    private void fillWpc(GridPane gridPane, ClientWpc wpc){
        for (ClientCell cell : wpc.getSchema()) {

            int row = cell.getCellPosition().getRow();
            int column = cell.getCellPosition().getColumn();

            AnchorPane cellXY = new AnchorPane();
            ClientColor color = cell.getCellColor();
            fillColor(cellXY, color);

            int number = cell.getCellNumber();
            fillNumber(cellXY, number);
            cellXY.setId(String.valueOf(row) + String.valueOf(column));
            gridPane.add(cellXY, column, row);
            schema.add(cellXY);
        }
    }


    private void setTurn() {
        Platform.runLater(() -> {
            roundLabel.setText("Round numero " + round + ", turno di " + username);
            endTurnButton.setDisable(false);
            extractedDicesGrid.setDisable(false);
            //setDices();
        });
    }

    private void fillNumber(AnchorPane cell, int number) {
        String style = "num".concat(String.valueOf(number));
        switch (number) {
            case 0:
                cell.getStyleClass().add("white");
                break;
            default:
                cell.getStyleClass().add(style);
                break;
        }
    }


    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null)
            cell.getStyleClass().add(String.valueOf(color).toLowerCase());
        else cell.getStyleClass().add("white");
    }

    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) { ((Notification) arg).handle(this); }


    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {}

    @Override
    public void handle(PlayersChangedNotification notification) {}

    @Override
    public void handle(WpcsExtractedNotification notification) {}

    @Override
    public void handle(PrivateObjExtractedNotification notification) {}

    @Override
    public void handle(UserPickedWpcNotification notification) {}

    @Override
    public void handle(ToolcardsExtractedNotification notification) {}

    @Override
    public void handle(PocsExtractedNotification notification) {}

    @Override
    public void handle(NewRoundNotification notification) {
        //dicesExtracted = true;
        System.out.println("Round: " + notification.roundNumber);
        System.out.println("Round: " + notification.roundNumber);
        System.out.println("Dadi estratti: ");
        for (ClientDice dice : notification.extractedDices){
            System.out.println("ID: " + dice.getDiceID() + "\t" + dice.getDiceNumber() + " " + dice.getDiceColor());
        }
    }

    @Override
    public void handle(NextTurnNotification notification){
        System.out.println("Turno: " + notification.turnNumber + "\tRound: " + clientModel.getCurrentRound());
        System.out.println("utente Attivo " + notification.activeUser);
        /*synchronized (waiter){
            if (clientModel.isActive()) waiter.notifyAll();
        }*/
    }

    @Override
    public void handle(DiceChangedNotification notification) {}

    @Override
    public void handle(DicePlacedNotification notification) {}

    @Override
    public void handle(ToolCardCanceledNotification notification) {}

    @Override
    public void handle(ToolCardUsedNotification notification) {}

    @Override
    public void handle(PlayerSkipTurnNotification notification) {}

}