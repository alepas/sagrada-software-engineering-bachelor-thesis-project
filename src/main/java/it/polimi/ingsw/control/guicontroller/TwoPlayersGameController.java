package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
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

import java.util.*;

import static java.lang.Thread.sleep;

public class TwoPlayersGameController implements Observer, NotificationHandler {

    private NetworkClient networkClient;
    private ClientModel clientModel;
    private String username;
    private ArrayList<ClientToolCard> toolCardsIDs = new ArrayList<>();
    private ArrayList<ClientPoc> pocIDs = new ArrayList<>();
    private String round;
    private ArrayList<ImageView> dices = new ArrayList<>();
    private ArrayList<AnchorPane> schema = new ArrayList<>();
    private HashMap<String, ClientWpc> wpc;

    @FXML
    private AnchorPane privateObjPane;

    @FXML
    private AnchorPane zoomedCard;

    @FXML private Button endTurnButton;

    @FXML private Button zoomCardBackButton;

    @FXML
    private Button zoomPoc1;

    @FXML
    private Button zoomPoc2;

    @FXML
    private Button zoomPoc3;

    @FXML
    private Button zoomTool1;

    @FXML
    private Button useTool1;

    @FXML
    private Button zoomTool2;

    @FXML
    private Button useTool2;

    @FXML
    private Button zoomTool3;

    @FXML
    private Button useTool3;

    @FXML
    private GridPane extractedDicesGrid;

    @FXML
    private GridPane firstWpcGrid;

    @FXML
    private GridPane pocGrid;

    @FXML private GridPane roundTrackGrid;

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

    /**
     * Replaces the constructor, it cointains also all the events done cliking the some objects in the view
     */
    @FXML
    void initialize() {
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();
        clientModel.addObserver(this);

        //game parameters
        username = clientModel.getUsername();
        toolCardsIDs = clientModel.getGameToolCards();
        pocIDs = clientModel.getGamePublicObjectiveCards();
        wpc = clientModel.getWpcByUsername();

        //set all the elements in the game view
        setToolCard();
        setPoc();
        setPrivateObjective(clientModel.getPrivateObjectives());
        setDices(clientModel.getExtractedDices());
        //diceAnimation();
        setWpc();
        waitForTurn();
        dragAndDrop();

        toolCardButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            privateObjPane.setVisible(false);
            roundTrackGrid.setVisible(false);
            toolCardGrid.setVisible(true);
        });

        pocButton.setOnAction(event -> {
            privateObjPane.setVisible(false);
            toolCardGrid.setVisible(false);
            pocGrid.setVisible(true);
            roundTrackGrid.setVisible(false);
        });

        roundTrackButton.setOnAction(event -> {
            privateObjPane.setVisible(false);
            toolCardGrid.setVisible(false);
            pocGrid.setVisible(false);
            roundTrackGrid.setVisible(true);
        });

        privateObjectiveButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            toolCardGrid.setVisible(false);
            privateObjPane.setVisible(true);
            roundTrackGrid.setVisible(false);
        });

        zoomTool1.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(0).getId())));

        zoomTool2.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(1).getId())));

        zoomTool3.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(2).getId())));

        zoomPoc1.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(0).getId())));

        zoomPoc2.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(1).getId())));

        zoomPoc3.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(2).getId())));

        zoomCardBackButton.setOnAction(event -> zoomedCard.setVisible(false));

        useTool1.setOnAction(event -> {
            try {
                useToolCard(toolCardsIDs.get(0).getId());
            } catch (CannotUseToolCardException e) {
                e.printStackTrace();
            }
        });

        useTool2.setOnAction(event -> {
            try {
                useToolCard(toolCardsIDs.get(1).getId());
            } catch (CannotUseToolCardException e) {
                e.printStackTrace();
            }
        });

        useTool3.setOnAction(event -> {
            try {
                useToolCard(toolCardsIDs.get(2).getId());

            } catch (CannotUseToolCardException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * This method contains four different actions which make the drag and drop action possible.
     * It has the goal to support the movement of a dice from the dice area to the schema player in a correct way.
     *
     * This method doesn't need parameters and return void.
     */
    private void dragAndDrop() {
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
                    Platform.runLater(()->extractedDicesGrid.getChildren().remove(dice));
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
                    int row = Integer.parseInt(cell.getId().substring(0, 1));
                    int column = Integer.parseInt(cell.getId().substring(1, 2));
                    Position position = new Position(row, column);
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
                networkClient.passTurn(clientModel.getUserToken());
                waitForTurn();
            } catch (CannotFindPlayerInDatabaseException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Calls the corresponding method in the networkClient
     *
     * @param id it's the toolCard's id
     * @throws CannotUseToolCardException suppose it's the same of the second one
     * @exception CannotFindPlayerInDatabaseException it is thrown if it is not possible to find the player because
     * he/she could be disonnected.
     * @exception CannotPerformThisMoveException is thrown if the player has already used a toolcard during his/hur turn
     * or if the favour's number isn't sufficent.
     * @exception PlayerNotAuthorizedException is never thrown because the Button are disable
     */
    private void useToolCard(String id) throws CannotUseToolCardException {
        try {
            networkClient.useToolCard(clientModel.getUserToken(), id);
        } catch (CannotFindPlayerInDatabaseException | CannotPerformThisMoveException | PlayerNotAuthorizedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets an <code>AnchorPane</code>, with the same <code>styleClass</code> of the Card that the user want to zoom,
     * inside the <codez>zoomedCard</code> pane which has been defined in the fxml. Both the zoomedCard and the zoomCardBackButton
     * are setted to visible.
     *
     * @param id ID of the toolCard or the PrivateObjectCard that the user wants to zoom
     */
    private void setZoomedCard(String id) {
        Platform.runLater(() -> {
            AnchorPane toolCard = new AnchorPane();
            toolCard.setPrefSize(400, 540);
            toolCard.setId(id);

            zoomedCard.getChildren().add(toolCard);
            zoomCardBackButton.toFront();
            zoomedCard.setVisible(true);
        });
    }


    /**
     * Waits the end of turn of all the other players; when the user is active, it means when it's his/her turn to play
     * the thread ends and the user goes to next method which is setTurn()
     */
    private void waitForTurn() {
        round = String.valueOf(clientModel.getCurrentRound());
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


    /*private void diceAnimation(){
        for (ClientDice dice : extractedDices) {
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
        }
    }*/


    /**
     * Sets in the extractedDicesGrid an ImageView for each dice which is in the ArrayList.
     *
     * @param extractedDices Arraylist formed by the ClientDices which has been extracted by the dicebag at the
     *                       biginning of every round.
     */
    private void setDices(ArrayList<ClientDice> extractedDices ) {
        for(int row = 0; row < extractedDices.size(); row++) {
            ImageView dice = setDiceStyle(extractedDices.get(row));
            dice.setFitHeight(100);
            dice.setFitWidth(100);
            dices.add(dice);
            extractedDicesGrid.add(dice, 0, row);
        }
    }


    /**
     * Sets the privateObjPane's id with the concat of a symbolic string and the color which correspond to the private
     * objective assigned to the player.
     *
     * @param privateObjective ArrayList formed by the Private Objectives assigned to the player.
     */
    private void setPrivateObjective(ClientColor[] privateObjective) {
        for(ClientColor color: privateObjective){
            String style = "prOC".concat(String.valueOf(color).toLowerCase());
            privateObjPane.setId(style);
        }
    }


    /**
     * Sets an anchorpane inside the pocGrid foreach POC selected at the biginning of the game.
     */
    private void setPoc() {
        for(int poc = 0; poc < pocIDs.size(); poc++ ) {
            String id = pocIDs.get(poc).getId();
            int finalPoc = poc;
            Platform.runLater(() -> {
                AnchorPane POC = new AnchorPane();
                String style = "poc" + String.valueOf(id);
                POC.setId(style);
                pocGrid.add(POC, 0, finalPoc);
            });
        }
    }


    /**
     * Sets an anchorpane inside the toolCardGrid foreach toolCard selected at the biginning of the game.
     */
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
            });
        }
    }


    /**
     * Chooses in which Grid must be set each schema: the firstWpcGrid is always associated to the schema of the username
     * associated to the specific controller.
     */
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


    /**
     * Sets an anchorPane in each position of the gridPane foreach Clientcell in the schema: it calls both
     * fillNumber() and fillColor() methods.
     *
     * @param gridPane is the id of the grid that is going to be filled
     * @param wpc is the schema containing all the information that will be used to fill the gridPane
     */
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

            if(cell.getCellDice()!= null){
                ImageView dice = setDiceStyle(cell.getCellDice());
                if(clientModel.getMyWpc() == wpc) {
                    dice.setFitWidth(70);
                    dice.setFitHeight(70);
                }else{
                        dice.setFitWidth(50);
                        dice.setFitHeight(50);
                }
                cellXY.getChildren().add(dice);
            }
        }
    }


    /**
     * sets the ImageView with all the information of the dice given as parameter.
     *
     * @param dice is the element that contains all the information of a dice
     * @return the ImageView which reppresents the dice given as parameter
     */
    private ImageView setDiceStyle(ClientDice dice){
        String col = dice.getDiceColor().toString().toLowerCase();
        String num = String.valueOf(dice.getDiceNumber());
        String id = String.valueOf(dice.getDiceID());
        String style = col.concat(num);
        ImageView image = new ImageView();
        image.getStyleClass().add(style);
        image.setId(id);
        return image;
    }


    /**
     *Changes the label information and sets Able both the endTurnButton and the extractedDicesGrid.
     */
    private void setTurn() {
        round = String.valueOf(clientModel.getCurrentRound());
        String turn = String.valueOf(clientModel.getCurrentTurn());
        Platform.runLater(() -> {
            roundLabel.setText("Round numero " + round +" turno numero "+ turn + ", turno di " + username);
            endTurnButton.setDisable(false);
            extractedDicesGrid.setDisable(false);
        });
    }


    /**
     * Sets the style of a cell:
     * if number =0 there aren't number restrictions, the method fills the pane in white
     * if number !=0 the method sets the style of the cell with the ImageView representing the restriction
     *
     * @param cell is the anchorPane created foreach cell in fillWPC() method
     * @param number is the integer that represents the possible number restriction of the cell
     */
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

    /**
     * Sets the style of a cell:
     * if color =null there aren't color restrictions, the method fills the pane in white
     * if color !=null the method sets the style of the cell with the ImageView representing the restriction
     *
     * @param cell is the anchorPane created foreach cell in fillWPC() method
     * @param color represents the color that may restric the cell
     */
    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null)
            cell.getStyleClass().add(String.valueOf(color).toLowerCase());
        else cell.getStyleClass().add("white");
    }


    /**
     * Moves all dices left during the round in the Round Track, removes them from the extractedDicesGrid and calls a
     * few methods to start the new round.
     *
     * @param round represents the current round number
     * @param extractedDices Arraylist formed by the ClientDices which has been extracted by the dicebag at the
     *                       biginning of every round.
     */
    private void nextRound(int round, ArrayList<ClientDice> extractedDices) {
        Platform.runLater(() -> {
            for (int column = 0; column < extractedDicesGrid.getChildren().size(); column++) {
                Node image = extractedDicesGrid.getChildren().get(column);
                ImageView dice = new ImageView();
                dice.setId(image.getId());
                dice.getStyleClass().add(image.getStyleClass().get(1));
                dice.setFitHeight(30);
                dice.setFitWidth(30);
                roundTrackGrid.add(dice, column+1, round);
            }
            extractedDicesGrid.getChildren().removeAll(extractedDicesGrid.getChildren());
            dices.clear();
            setDices(extractedDices);
            dragAndDrop();
            waitForTurn();
        });
    }

    private void setScore(ArrayList<Map.Entry<String, Integer>> score){
        Platform.runLater(()->{
            if (score.get(0).getKey().equals(clientModel.getUsername()))
                System.out.println("U won!");
            else
                System.out.println("u loose");

        });


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
        System.out.println("Round: " + notification.roundNumber);
        int round = notification.roundNumber -1;
        nextRound(round, notification.extractedDices);
    }

    @Override
    public void handle(NextTurnNotification notification){
        System.out.println("Turno: " + notification.turnNumber + "\tRound: " + clientModel.getCurrentRound());
        System.out.println("utente Attivo " + notification.activeUser);
    }

    @Override
    public void handle(DiceChangedNotification notification) {}

    @Override
    public void handle(DicePlacedNotification notification) {
        Platform.runLater(()->{
            String user = notification.username;
            String id = String.valueOf(notification.dice.getDiceID());
            if(user.equals(secondUserLabel.getText()))
                fillWpc(secondWpcGrid, notification.wpc);
            for(int i= 0; i< extractedDicesGrid.getChildren().size(); i++){
                if(extractedDicesGrid.getChildren().get(i).getId().equals(id))
                    extractedDicesGrid.getChildren().remove(i);
            }
        });
    }

    @Override
    public void handle(ToolCardCanceledNotification notification) {}

    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {}

    @Override
    public void handle(ScoreNotification notification) {
        ArrayList<Map.Entry<String, Integer>> scores = new ArrayList<>(notification.scoreList.entrySet());
        Collections.sort(scores, new Comparator<Map.Entry<?, Integer>>() {
            @Override
            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        setScore(scores);
    }

}