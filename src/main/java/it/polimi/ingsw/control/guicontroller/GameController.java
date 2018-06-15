package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import it.polimi.ingsw.view.Status;
import it.polimi.ingsw.model.clientModel.ClientWpcConstants;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.model.clientModel.ClientConstants.NUM_OF_ROUNDS;
import static it.polimi.ingsw.model.clientModel.ClientDiceLocations.EXTRACTED;
import static it.polimi.ingsw.model.clientModel.ClientDiceLocations.ROUNDTRACK;
import static it.polimi.ingsw.model.clientModel.ClientDiceLocations.WPC;
import static it.polimi.ingsw.view.Status.*;
import static java.lang.Thread.sleep;

public class GameController implements Observer, NotificationHandler {

    @FXML private GridPane fourthWpcGrid;
    @FXML private Label fourthFavourLabel;
    @FXML private Label fourthUserLabel;
    @FXML private ImageView diceBagIcon;
    @FXML private ImageView usedTool3Icon;
    @FXML private ImageView usedTool2Icon;
    @FXML private Label secondWpcNameLabel;
    @FXML private ImageView usedTool1Icon;
    @FXML private Label firstWpcNameLabel;
    @FXML private Label message1Label;
    @FXML
    private AnchorPane roundTrackPane;
    @FXML
    private Label secondFavourLabel;
    @FXML
    private Label firstFavourLabel;
    @FXML
    private Button cancelActionButton;
    @FXML
    private AnchorPane plusMinusPane;
    @FXML
    private AnchorPane changeNumberPane;
    @FXML
    private Circle oneCircle;
    @FXML
    private Circle twoCircle;
    @FXML
    private Circle threeCircle;
    @FXML
    private Circle fourCircle;
    @FXML
    private Circle fiveCircle;
    @FXML
    private Circle sixCircle;

    @FXML
    private ImageView minusOneIcon;
    @FXML
    private ImageView plusOneIcon;
    private NetworkClient networkClient;
    private ClientModel clientModel;
    private String username;
    private ArrayList<ClientToolCard> toolCardsIDs = new ArrayList<>();
    private ArrayList<ClientPoc> pocIDs = new ArrayList<>();
    private String round;
    private ArrayList<ImageView> extractDices = new ArrayList<>();
    private ArrayList<ImageView> schemaDices = new ArrayList<>();
    private ArrayList<ImageView> roundTrackDices = new ArrayList<>();
    private ArrayList<AnchorPane> schema = new ArrayList<>();
    private HashMap<String, ClientWpc> wpc;
    private final Object waiter = new Object();
    private boolean isUsedToolCard = false;
    private NextAction lastNextAction;

    @FXML
    private Label messageLabel;
    @FXML
    private Button personalAreaButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label firstLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private Label firstScoreLabel;
    @FXML
    private Label secondScoreLabel;
    @FXML
    private AnchorPane boardGame;
    @FXML
    private AnchorPane scoreBoard;

    @FXML
    private AnchorPane privateObjPane;

    @FXML
    private AnchorPane zoomedCard;

    @FXML
    private Button endTurnButton;

    @FXML
    private Button zoomCardBackButton;

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

    @FXML
    private GridPane roundTrackGrid;

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
    @FXML  private Label thirdUserLabel;
    @FXML private  Label thirdFavourLabel;
    @FXML private GridPane thirdWpcGrid;

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
        startGame();

        //menuButton: if the player choose to see the tool cards he/she activate this lambda by clicking on the Tool Cards button
        toolCardButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            privateObjPane.setVisible(false);
            roundTrackPane.setVisible(false);
            toolCardGrid.setVisible(true);
        });

        //menuButton: if the player choose to see the public Objective cards he/she activate this lambda by clicking on the POC button
        pocButton.setOnAction(event -> {
            privateObjPane.setVisible(false);
            toolCardGrid.setVisible(false);
            pocGrid.setVisible(true);
            roundTrackPane.setVisible(false);
        });

        //menuButton: if the player choose to see the Round Track he/she activate this lambda by clicking on the Round Track button
        roundTrackButton.setOnAction(event -> {
            privateObjPane.setVisible(false);
            toolCardGrid.setVisible(false);
            pocGrid.setVisible(false);
            roundTrackPane.setVisible(true);
        });

        //menuButton: if the player choose to see his/her private color he/she activate this lambda by clicking on the Private Obj button
        privateObjectiveButton.setOnAction(event -> {
            pocGrid.setVisible(false);
            toolCardGrid.setVisible(false);
            privateObjPane.setVisible(true);
            roundTrackPane.setVisible(false);
        });

        //by clicking on the zoom button the player sees the related tool Card or the related POC bigger
        zoomTool1.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(0).getId())));

        zoomTool2.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(1).getId())));

        zoomTool3.setOnAction(event -> setZoomedCard("tool".concat(toolCardsIDs.get(2).getId())));

        zoomPoc1.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(0).getId())));

        zoomPoc2.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(1).getId())));

        zoomPoc3.setOnAction(event -> setZoomedCard("poc".concat(pocIDs.get(2).getId())));

        //by clicking on the back button th zooming area isn't visible anymore
        zoomCardBackButton.setOnAction(event -> zoomedCard.setVisible(false));

        //by clicking on the use button the player starts to use the the related tool card
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

        //by clicking on the end button the player pass his/her turn
        endTurnButton.setOnAction(event -> {
            try {
                networkClient.passTurn(clientModel.getUserToken());
            } catch (CannotFindPlayerInDatabaseException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
                message1Label.setText(e.getMessage());
            }
            lastNextAction = NextAction.WAIT_FOR_TURN;
            stateAction(ANOTHER_PLAYER_TURN);
        });

        //by clicking on the cancel button the player goes back to the previous action
        cancelActionButton.setOnAction(event -> stateAction(INTERRUPT_TOOLCARD));

        //by clicking on the personal area button the player changes scene and goes to the area containing all his/her info
        personalAreaButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/PersonalAreaScene.fxml"));

        //by clicking on the new game button the player starts a new game
        newGameButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SetNewGameScene.fxml"));
    }

    /**
     * Calls all the setting methods, if the player is coming back after a disconnection it sets the last action.
     */
    private void startGame() {
        setToolCard();
        setPoc();
        setPrivateObjective(clientModel.getPrivateObjectives());
        setDices(clientModel.getExtractedDices());
        setWpc();

        try {
            NextAction nextAction = networkClient.getUpdatedGame(clientModel.getUserToken());
            if (nextAction == null) {
                lastNextAction = NextAction.WAIT_FOR_TURN;
                stateAction(ANOTHER_PLAYER_TURN);
            } else {
                updateGraphicRoundTrack();
                lastNextAction = nextAction;
                stateAction(Objects.requireNonNull(Status.change(nextAction)));
            }
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        }
        dragAndDrop();
    }

    /**
     * Class different methods depending on the state in which the player game is.
     *
     * @param state is the state in which the player is.
     */
    private synchronized void stateAction(Status state) {
        System.out.println("state: "+state);
        switch (state) {
            case ANOTHER_PLAYER_TURN:
                waitForTurn();
                break;
            case MENU_ALL:
                playTurn();
                break;
            case MENU_ONLY_PLACEDICE:
                possibleActionPlaceDice();
                break;
            case MENU_ONLY_TOOLCARD:
                possibleActionUseToolCard();
                break;
            case MENU_ONLY_ENDTURN:
                possibleActionEndTurn();
                break;
            case INTERRUPT_TOOLCARD:
                cancelLastOperation();
                break;
            case SELECT_DICE_TOOLCARD:
                isChosenDiceIdActive();
                pickDiceForToolCard();
                break;
            case SELECT_NUMBER_TOOLCARD:
                isChosenDiceIdActive();
                selectNumberToolCard();
                break;
            case SELECT_DICE_TO_ACTIVE_TOOLCARD:

                //TODO
                break;
            case PLACE_DICE_TOOLCARD:
                isChosenDiceIdActive();
                placeDiceWithToolCard();
                break;
            default:
                break;
        }
    }

    /**
     * This method contains four different actions which make the drag and drop action possible.
     * It has the goal to support the movement of a dice from the dice area to the schema player in a correct way.
     *
     * This method doesn't need parameters and return void.
     */
    private void dragAndDrop() {
        for (ImageView dice : extractDices) {
            dice.setOnDragDetected(event -> {
                System.out.println("drag detected");
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
                    Platform.runLater(() -> extractedDicesGrid.getChildren().remove(dice));
                } else
                    dice.setVisible(true);
                event.consume();
            });
        }


        for (ImageView dice : schemaDices) {
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
                    Platform.runLater(() -> extractedDicesGrid.setDisable(false));
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
                    NextAction nextAction = placeDice(cell, id);
                    stateAction(Objects.requireNonNull(change(nextAction)));
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateGraphicMyWpc();
                    for (ClientCell cells : clientModel.getMyWpc().getSchema())
                        if (cells.getCellDice() != null && cells.getCellDice().getDiceID() == id) success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }
    }

    /**
     * Calls the network method that will check if it's possible to add the dice in the chosen position.
     *
     * @param cell is the AnchorPane where the player want to add the dice
     * @param id   is the dice ID
     * @return the next action that the player can do after placing the dice
     */
    private NextAction placeDice(AnchorPane cell, int id) {
        int row = Integer.parseInt(cell.getId().substring(0, 1));
        int column = Integer.parseInt(cell.getId().substring(1, 2));
        Position position = new Position(row, column);
        NextAction nextAction;
        try {
            if (!isUsedToolCard)
                nextAction = networkClient.placeDice(clientModel.getUserToken(), id, position);
            else {
                nextAction = networkClient.placeDiceForToolCard(clientModel.getUserToken(), id, position);
                isUsedToolCard = false;
            }
            lastNextAction = nextAction;
        } catch (CannotFindPlayerInDatabaseException | CannotPickPositionException |
                CannotPickDiceException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
            Platform.runLater(() -> message1Label.setText(e.getMessage()));
            if (isUsedToolCard) isUsedToolCard = false;
            return lastNextAction;
        } catch (NoToolCardInUseException e) {
            message1Label.setText("Non stai usando alcuna Tool Card!");
            return lastNextAction;
        }
        return nextAction;
    }

    /**
     * Calls the corresponding method in the networkClient
     *
     * @param id it's the toolCard's id
     * @throws CannotUseToolCardException suppose it's the same of the second one
     */
    private void useToolCard(String id) throws CannotUseToolCardException {
        cancelActionButton.setVisible(true);
        endTurnButton.setVisible(false);
        try {
            NextAction nextAction = networkClient.useToolCard(clientModel.getUserToken(), id);
            message1Label.setText("Stai usando la ToolCard" + id + "!");
            lastNextAction = nextAction;
            updateGraphicsCurrentUser();
            stateAction(Objects.requireNonNull(change(nextAction)));
        } catch (CannotFindPlayerInDatabaseException | CannotPerformThisMoveException | PlayerNotAuthorizedException e) {
            message1Label.setText(e.getMessage());
            stateAction(Objects.requireNonNull(change(lastNextAction)));
        }
    }

    /**
     * Sets an AnchorPane, with the same styleClass of the Card that the user want to zoom,
     * inside the zoomedCard pane which has been defined in the fxml. Both the zoomedCard and the zoomCardBackButton
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
        Thread waitTurn = new Thread(() -> {
            while (!clientModel.isActive()) {
                Platform.runLater(() -> {
                    roundLabel.setText("Round numero " + round + ", in attesa del proprio turno..");
                    messageLabel.setText("Attendi il tuo turno.");
                    for (Node dice : extractedDicesGrid.getChildren())
                        dice.setDisable(true);
                    useTool1.setDisable(true);
                    useTool2.setDisable(true);
                    useTool3.setDisable(true);
                    endTurnButton.setVisible(true);
                    endTurnButton.setDisable(true);
                    cancelActionButton.setVisible(false);
                });
                synchronized (waiter) {
                    try {
                        waiter.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            lastNextAction = NextAction.MENU_ALL;
            stateAction(MENU_ALL);
        });
        waitTurn.start();
    }

    /**
     * Changes the label information and sets Able both the endTurnButton and the extractedDicesGrid.
     */
    private void playTurn() {
        round = String.valueOf(clientModel.getCurrentRound());
        String turn = String.valueOf(clientModel.getCurrentTurn());
        Platform.runLater(() -> {
            if (turn.equals("3")) messageLabel.setText("Tocca ancora a te!");
            else messageLabel.setText("Tocca a te!");
            message1Label.setText("");
            roundLabel.setText("Round numero " + round + ", turno di " + username);
            endTurnButton.setVisible(true);
            endTurnButton.setDisable(false);
            cancelActionButton.setVisible(false);
            for (Node dice : extractedDicesGrid.getChildren())
                dice.setDisable(false);
            extractedDicesGrid.setDisable(false);
            useTool1.setDisable(false);
            useTool2.setDisable(false);
            useTool3.setDisable(false);
            dragAndDrop();
        });
    }

    /**
     * Sets in the extractedDicesGrid an ImageView for each dice which is in the ArrayList.
     *
     * @param extractedDices Arraylist formed by the ClientDices which has been extracted by the dicebag at the
     *                       biginning of every round.
     */
    private void setDices(ArrayList<ClientDice> extractedDices) {
        extractDices.clear();
        extractedDicesGrid.getChildren().clear();
        for (int row = 0; row < extractedDices.size(); row++) {
            ImageView dice = setDiceStyle(extractedDices.get(row));
            dice.setFitHeight(100);
            dice.setFitWidth(100);
            extractDices.add(dice);
            extractedDicesGrid.add(dice, 0, row);
        }
        extractedDicesGrid.setDisable(false);
    }

    /**
     * Sets the privateObjPane's id with the concat of a symbolic string and the color which correspond to the private
     * objective assigned to the player.
     *
     * @param privateObjective ArrayList formed by the Private Objectives assigned to the player.
     */
    private void setPrivateObjective(ClientColor[] privateObjective) {
        for (ClientColor color : privateObjective) {
            String style = "prOC".concat(String.valueOf(color).toLowerCase());
            privateObjPane.setId(style);
        }
    }

    /**
     * Sets an anchor pane inside the pocGrid foreach POC selected at the beginning of the game.
     */
    private void setPoc() {
        for (int poc = 0; poc < pocIDs.size(); poc++) {
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
    private void setToolCard() {
        for (int tool = 0; tool < toolCardsIDs.size(); tool++) {
            String id = toolCardsIDs.get(tool).getId();
            int finalTool = tool;
            Platform.runLater(() -> {
                AnchorPane toolCard = new AnchorPane();
                String typeCard = "tool";
                String number = String.valueOf(id);
                String style = typeCard.concat(number);

                toolCard.setId(style);
                toolCardGrid.add(toolCard, 0, finalTool);
            });
        }
    }

    /**
     * Chooses in which Grid must be set each schema: the firstWpcGrid is always associated to the schema of the username
     * associated to the specific controller.
     */
    private void setWpc() {
        int numPlayer = 1;
        switch (wpc.size()) {
            case 1:
                firstUserLabel.setText(username);
                fillWpc(firstWpcGrid, wpc.get(username));
                firstWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(username).getWpcID()));
                break;
            case 2:
                for (String wpcUser : wpc.keySet()) {
                    if (wpcUser.equals(username)) {
                        firstUserLabel.setText(username);
                        firstFavourLabel.setText(String.valueOf(wpc.get(username).getFavours()).concat("X"));
                        firstWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(username).getWpcID()));
                        fillWpc(firstWpcGrid, wpc.get(wpcUser));
                    } else {
                        secondUserLabel.setText(wpcUser);
                        secondFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        secondWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(wpcUser).getWpcID()));
                        fillWpc(secondWpcGrid, wpc.get(wpcUser));
                    }
                }
                break;
            case 3:
                for (String wpcUser : wpc.keySet()) {
                    if (wpcUser.equals(username)) {
                        firstUserLabel.setText(username);
                        firstFavourLabel.setText(String.valueOf(wpc.get(username).getFavours()).concat("X"));
                        firstWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(username).getWpcID()));
                        fillWpc(firstWpcGrid, wpc.get(wpcUser));
                    } else if(!wpcUser.equals(username) && numPlayer == 1) {
                        secondUserLabel.setText(wpcUser);
                        secondFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        secondWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(wpcUser).getWpcID()));
                        fillWpc(secondWpcGrid, wpc.get(wpcUser));
                        numPlayer = 2;
                    }
                    else if(!wpcUser.equals(username) && numPlayer == 2) {
                        thirdUserLabel.setText(wpcUser);
                        thirdFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        fillWpc(thirdWpcGrid, wpc.get(wpcUser));
                    }
                }
                break;
            case 4:
                for (String wpcUser : wpc.keySet()) {
                    if (wpcUser.equals(username)) {
                        firstUserLabel.setText(username);
                        firstFavourLabel.setText(String.valueOf(wpc.get(username).getFavours()).concat("X"));
                        firstWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(username).getWpcID()));
                        fillWpc(firstWpcGrid, wpc.get(wpcUser));
                    } else if(!wpcUser.equals(username) && numPlayer == 1) {
                        secondUserLabel.setText(wpcUser);
                        secondFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        secondWpcNameLabel.setText(ClientWpcConstants.setWpcName(wpc.get(wpcUser).getWpcID()));
                        fillWpc(secondWpcGrid, wpc.get(wpcUser));
                        numPlayer = 2;
                    }
                    else if(!wpcUser.equals(username) && numPlayer == 2) {
                        thirdUserLabel.setText(wpcUser);
                        thirdFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        fillWpc(thirdWpcGrid, wpc.get(wpcUser));
                        numPlayer = 3;
                    }
                    else if(!wpcUser.equals(username) && numPlayer == 3) {
                        fourthUserLabel.setText(wpcUser);
                        fourthFavourLabel.setText(String.valueOf(wpc.get(wpcUser).getFavours()).concat("X"));
                        fillWpc(fourthWpcGrid, wpc.get(wpcUser));
                    }
                }
                break;
        }

    }

    /**
     * Sets an anchorPane in each position of the gridPane foreach Clientcell in the schema: it calls both
     * fillNumber() and fillColor() methods.
     *
     * @param gridPane is the id of the grid that is going to be filled
     * @param wpc      is the schema containing all the information that will be used to fill the gridPane
     */
    private void fillWpc(GridPane gridPane, ClientWpc wpc) {
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

            if (cell.getCellDice() != null) {
                ImageView dice = setDiceStyle(cell.getCellDice());
                if (clientModel.getMyWpc() == wpc) {
                    schemaDices.add(dice);
                    dice.setFitWidth(70);
                    dice.setFitHeight(70);
                } else {
                    if(clientModel.getGameNumPlayers() == 2) {
                        dice.setFitWidth(50);
                        dice.setFitHeight(50);
                    }
                    else{
                        dice.setFitWidth(43);
                        dice.setFitHeight(43);
                    }
                }
                cellXY.getChildren().add(dice);
            }
        }
        if(lastNextAction == NextAction.MENU_ONLY_PLACE_DICE)
            dragAndDrop();
    }

    /**
     * sets the ImageView with all the information of the dice given as parameter.
     *
     * @param dice is the element that contains all the information of a dice
     * @return the ImageView which reppresents the dice given as parameter
     */
    private ImageView setDiceStyle(ClientDice dice) {
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
     * Sets the style of a cell:
     * if number =0 there aren't number restrictions, the method fills the pane in white
     * if number !=0 the method sets the style of the cell with the ImageView representing the restriction
     *
     * @param cell   is the anchorPane created foreach cell in fillWPC() method
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
     * @param cell  is the anchorPane created foreach cell in fillWPC() method
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
     */
    private void nextRound() {
        Platform.runLater(() -> {
            updateGraphicRoundTrack();
            setDices(clientModel.getExtractedDices());
            dragAndDrop();
            lastNextAction = NextAction.WAIT_FOR_TURN;
            stateAction(ANOTHER_PLAYER_TURN);
        });
    }

    /**
     * It's called when the player has placed a dice; the only things he/she can do are pass the turn or use a tool card.
     */
    private void possibleActionUseToolCard() {
        endTurnButton.setVisible(true);
        cancelActionButton.setVisible(false);
        extractedDicesGrid.setDisable(true);
        messageLabel.setText("Usa una ToolCard o termina il turno!");
    }

    /**
     * It's called when the player as already used a toolcard, the only things he/she can do are to placea dice or to
     * end the turn.
     */
    private void possibleActionPlaceDice() {
        message1Label.setText("");
        for (Node dice : extractedDicesGrid.getChildren())
            dice.setDisable(false);
        extractedDicesGrid.setDisable(false);
        endTurnButton.setVisible(true);
        cancelActionButton.setVisible(false);
        useTool1.setDisable(true);
        useTool2.setDisable(true);
        useTool3.setDisable(true);
        messageLabel.setText("Posiziona un dado o passa il turno!");
    }

    /**
     * the player has done all the possible actions that could do; this method set disable all elements of the window
     * except for the pass turn Button
     */
    private void possibleActionEndTurn() {
        message1Label.setText("");
        endTurnButton.setVisible(true);
        cancelActionButton.setVisible(false);
        for (Node dice : extractedDicesGrid.getChildren())
            dice.setDisable(true);
        extractedDicesGrid.setDisable(true);
        useTool1.setDisable(true);
        useTool2.setDisable(true);
        useTool3.setDisable(true);
        messageLabel.setText("Passa il turno!");
    }

    /**
     * Sets able or disabele the areas where the user has to take the dice.
     */
    private void placeDiceWithToolCard() {
        Platform.runLater(() -> {
            ToolCardClientNextActionInfo info = clientModel.getToolCardClientNextActionInfo();
            isUsedToolCard = true;
            switch (info.wherePickNewDice) {
                case WPC:
                    messageLabel.setText("Scegli un dado dallo schema e posizionalo.");
                    extractedDicesGrid.setDisable(true);
                    break;
                case EXTRACTED:
                    messageLabel.setText("Scegli un dado dalla riserva e posizionalo.");
                    for (Node dice : extractedDicesGrid.getChildren()) {
                        if (info.diceChosen != null){
                            if (!dice.getId().equals(String.valueOf(info.diceChosen.getDiceID())))
                                dice.setDisable(true);
                            else {
                                for (ClientDice clientDice : clientModel.getExtractedDices()) {
                                    if (String.valueOf(clientDice.getDiceID()).equals(dice.getId()))
                                        changeDiceStyle(dice, clientDice);
                                }
                            }
                        }
                    }
                    break;
            }
            dragAndDrop();
        });
    }

    /**
     * Changes the style of a dice.
     *
     * @param dice       is the imageView whose the player wants to change the style.
     * @param clientDice is the dice coming from the server with the changed style
     */
    private void changeDiceStyle(Node dice, ClientDice clientDice) {
        String col = clientDice.getDiceColor().toString().toLowerCase();
        String num = String.valueOf(clientDice.getDiceNumber());
        String style = col.concat(num);
        dice.getStyleClass().remove(dice.getStyle());
        dice.getStyleClass().add(style);
    }

    /**
     * Contains three possible events on three possible different actions that the player can do: the player can pick a
     * dice from the extracted dices, from the round track or from the his/her schema.
     */
    private void pickDiceForToolCard() {
        ToolCardClientNextActionInfo info = clientModel.getToolCardClientNextActionInfo();
        switch (info.wherePickNewDice) {
            case EXTRACTED:
                extractedDicesGrid.setDisable(false);
                messageLabel.setText("Clicca su un dado della riserva!");
                for (ImageView extractDice : extractDices) {
                    extractDice.setOnMouseClicked(event -> {
                        makeSelectedDiceVisible(extractDice, EXTRACTED);
                        NextAction nextAction = pickDice(Integer.parseInt(extractDice.getId()), EXTRACTED);
                        stateAction(Objects.requireNonNull(change(nextAction)));
                    });
                }
                break;
            case ROUNDTRACK:
                roundTrackGrid.setDisable(false);
                for (ImageView roundTrackDice : roundTrackDices)
                    roundTrackDice.setDisable(false);
                messageLabel.setText("Clicca su un dado del Round Track!");
                for (ImageView roundTrackDice : roundTrackDices) {
                    roundTrackDice.setOnMouseClicked(event -> {
                        System.out.println("dado roundTrack: " + roundTrackDice);
                        makeSelectedDiceVisible(roundTrackDice, ROUNDTRACK);
                        NextAction nextAction = pickDice(Integer.parseInt(roundTrackDice.getId()), ROUNDTRACK);
                        stateAction(Objects.requireNonNull(change(nextAction)));
                    });
                }
                break;

            case WPC:
                messageLabel.setText("Clicca su un dado del tuo schema di gioco!");
                for (ImageView schemaDice : schemaDices) {
                    schemaDice.setOnMouseClicked(event -> {
                        NextAction nextAction = pickDice(Integer.parseInt(schemaDice.getId()), WPC);
                        stateAction(Objects.requireNonNull(change(nextAction)));
                    });
                }
                break;
        }
    }

    /**
     * Calls the method pickDiceForToolCard in the networkClient.
     *
     * @param id is the dice's is the player want to pick
     * @return the next action that the player can do
     */
    private NextAction pickDice(int id, ClientDiceLocations locations) {
        NextAction nextAction = null;
        try {
            nextAction = networkClient.pickDiceForToolCard(clientModel.getUserToken(), id);
            System.out.println("pick dice:" +nextAction);
            switch (locations){
                case WPC:
                    updateGraphicMyWpc();
                    break;
                case ROUNDTRACK:
                    updateGraphicRoundTrack();
                    updateGraphicExtractedDices();
                    break;
                case EXTRACTED:
                    updateGraphicExtractedDices();
            }
        } catch (CannotFindPlayerInDatabaseException e) {
            messageLabel.setText(e.getMessage());
        } catch (CannotPickDiceException e) {
            messageLabel.setText(e.getMessage());
        } catch (PlayerNotAuthorizedException e) {
            messageLabel.setText(e.getMessage());
        } catch (NoToolCardInUseException e) {
            messageLabel.setText(e.getMessage());
        } catch (CannotPerformThisMoveException e) {
            messageLabel.setText(e.getMessage());
        } finally {
            if (nextAction != null) {
                lastNextAction = nextAction;
                return nextAction;
            }
            return lastNextAction;
        }

    }

    /**
     * makes the dice go back to the old style.
     *
     * @param dice is the imageView selected
     * @param clientDice is the old client dice with the old information of the selected dice
     */
    private void changeStyle(ImageView dice, ClientDice clientDice) {
        String col = clientDice.getDiceColor().toString().toLowerCase();
        String num = String.valueOf(clientDice.getDiceNumber());
        String style = col.concat(num);
        dice.getStyleClass().clear();
        dice.getStyleClass().add(style);
    }

    /**
     * Shows the icons to increase, decrease or change the dice's number; contains the lambda's action on those icons.
     */
    private void selectNumberToolCard() {
        ToolCardClientNextActionInfo info = clientModel.getToolCardClientNextActionInfo();
        for (ClientDice clientDice : clientModel.getExtractedDices()) {
            for (ImageView dice : extractDices){
                if (dice.getId().equals(String.valueOf(clientDice.getDiceID()))) changeStyle(dice, clientDice);}
        }
        if (info.numbersToChoose.size() <= 2) {
            messageLabel.setText("Aggiungi 1 o sottrai 1");
            plusMinusPane.setVisible(true);
            plusOneIcon.setVisible(true);
            minusOneIcon.setVisible(true);
            if(info.numbersToChoose.size() == 1){
                if(info.numbersToChoose.get(0) == 1) minusOneIcon.setVisible(false);
                else plusOneIcon.setVisible(false);
            }
            plusOneIcon.setOnMouseClicked(event -> {
                for (ClientDice dice : clientModel.getExtractedDices()) {
                    if (dice.getDiceID() == info.diceChosen.getDiceID()) pickNumberForToolCard(1);
                }
            });

            minusOneIcon.setOnMouseClicked(event -> {
                for (ClientDice dice : clientModel.getExtractedDices()) {
                    if (dice.getDiceID() == info.diceChosen.getDiceID()) pickNumberForToolCard(-1);
                }
            });
        } else {
            changeNumberPane.setVisible(true);

            oneCircle.setOnMouseClicked(event -> pickNumberForToolCard(1));
            twoCircle.setOnMouseClicked(event -> pickNumberForToolCard(2));
            threeCircle.setOnMouseClicked(event -> pickNumberForToolCard(3));
            fourCircle.setOnMouseClicked(event -> pickNumberForToolCard(4));
            fiveCircle.setOnMouseClicked(event -> pickNumberForToolCard(5));
            sixCircle.setOnMouseClicked(event -> pickNumberForToolCard(6));
        }
    }

    /**
     * Calls the network method that will call the server method which increase, decrease or modify the dice number.
     *
     * @param number is the number selected by the player
     */
    private void pickNumberForToolCard(int number) {
        NextAction nextAction;
        try {
            nextAction = networkClient.pickNumberForToolCard(clientModel.getUserToken(), number);
        } catch (CannotFindPlayerInDatabaseException | NoToolCardInUseException |
                PlayerNotAuthorizedException | CannotPerformThisMoveException | CannotPickNumberException e) {
            message1Label.setText(e.getMessage());
            nextAction = lastNextAction;
        }
        lastNextAction = nextAction;
        updateGraphicsCurrentUser();
        stateAction(Objects.requireNonNull(change(nextAction)));
        plusOneIcon.setVisible(true);
        minusOneIcon.setVisible(true);
        plusMinusPane.setVisible(false);
        changeNumberPane.setVisible(false);
    }

    /**
     * Sets disable all dices that are different from the one that as as id the diceChosen
     */
    private void isChosenDiceIdActive() {
        ToolCardClientNextActionInfo info = clientModel.getToolCardClientNextActionInfo();
        if (info.diceChosenLocation == WPC) {
            for (ImageView dice : schemaDices) {
                if (Integer.parseInt(dice.getId()) == (info.diceChosen.getDiceID())) makeSelectedDiceVisible(dice, WPC);
                else dice.setDisable(true);
            }
        } else if (info.diceChosenLocation == EXTRACTED) {
            for (ImageView dice : extractDices) {
                if (Integer.parseInt(dice.getId()) == (info.diceChosen.getDiceID())) makeSelectedDiceVisible(dice, EXTRACTED);
                else dice.setDisable(true);
            }
        } else if (info.diceChosenLocation == ROUNDTRACK) {
            for (ImageView dice : roundTrackDices) {
                if (Integer.parseInt(dice.getId()) == (info.diceChosen.getDiceID())) makeSelectedDiceVisible(dice, ROUNDTRACK);
                else dice.setDisable(true);
            }
        }
    }

    /**
     * Makes the dice selected by the player more visible to show and remeber to the player which dice has chosen during
     * the preview action.
     *
     * @param dice is the imageView clicked before by the user
     */
    private void makeSelectedDiceVisible(ImageView dice, ClientDiceLocations locations) {
        Platform.runLater(() -> {
            switch (locations){
                case EXTRACTED:
                    dice.setFitWidth(120.0);
                    dice.setFitHeight(120.0);
                    break;
                case ROUNDTRACK:
                    dice.setFitWidth(50.0);
                    dice.setFitHeight(50.0);
                    break;
                case WPC:
                    dice.setFitWidth(100.0);
                    dice.setFitHeight(100.0);
                    break;
            }
            dice.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.AQUAMARINE, 10, 0.0, 0.0, 0.0));
            dice.setDisable(false);
        });
    }

    /**
     * The selected dice comes back to the original style.
     */
    private void makeSelectedDiceLessVisible(ClientDiceLocations location) {
         switch (location) {
             case WPC:
                 updateGraphicMyWpc();
                 break;
             case EXTRACTED:
                 updateGraphicRoundTrack();
                 updateGraphicExtractedDices();
                 for(ImageView dice: extractDices)
                     dice.setDisable(false);
                 break;
            }
    }

    /**
     * Goes back to the previous action.
     */
    private void cancelLastOperation() {
        ToolCardClientNextActionInfo info = clientModel.getToolCardClientNextActionInfo();
        try {
            NextAction previousAction = networkClient.cancelAction(clientModel.getUserToken());
            switch (previousAction) {
                case MENU_ALL:
                    isUsedToolCard = false;
                    break;
                case SELECT_DICE_TOOLCARD:
                    plusMinusPane.setVisible(false);
                    changeNumberPane.setVisible(false);
                    makeSelectedDiceLessVisible(info.diceChosenLocation);
                    break;
                case SELECT_NUMBER_TOOLCARD:
                    plusMinusPane.setVisible(true);
                    break;
            }
            lastNextAction = previousAction;
        } catch (CannotCancelActionException e) {
            messageLabel.setText(e.getMessage());
        } catch (PlayerNotAuthorizedException e) {
            messageLabel.setText(e.getMessage());
        } catch (CannotFindPlayerInDatabaseException e) {
            messageLabel.setText(e.getMessage());
        }

        stateAction(Objects.requireNonNull(change(lastNextAction)));
    }



    //-------------------------------------- End Game Methods ------------------------------------------

    /**
     * Changes the scene in the window.
     *
     * @param event the event related to the desire of the player to change scene
     * @param path is the path of the next scene
     */
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

    /**
     * Sets the game invisible and shows to the users who won and who lost the game.
     *
     * @param score Arraylist composte by HashMAp containing the score and the username of each player, it is already sorted
     */
    private void setScore(ArrayList<Map.Entry<String, Integer>> score) {
        Platform.runLater(() -> {
            boardGame.setVisible(false);
            if (score.get(0).getKey().equals(clientModel.getUsername())) {
                firstLabel.setText("1. " + username);
                firstScoreLabel.setText(String.valueOf(score.get(0).getValue()));
                winnerLabel.setText("HAI VINTO!");
                secondLabel.setText("2. " + score.get(1).getKey());
                secondScoreLabel.setText(String.valueOf(score.get(1).getValue()));
            } else {
                firstLabel.setText("1. " + score.get(0).getKey());
                firstScoreLabel.setText(String.valueOf(score.get(0).getValue()));
                winnerLabel.setText("HAI PERSO!");
                secondLabel.setText("2. " + username);
                secondScoreLabel.setText(String.valueOf(score.get(1).getValue()));
            }
            scoreBoard.setVisible(true);
        });
    }

    //-------------------------------------- Update Methods ------------------------------------------

    /**
     * updates the extraced dices: if the size is > than the client model extraced ones it deletes the dices that are not
     * in the client model; if is = it searches for the different ones.
     */
    private synchronized void updateGraphicExtractedDices() {
        int tempIndex = -1;
        int newDiceIndex = -1;
        int tempPosition = -1;
        Node tempNode;
        extractDices.clear();
        ArrayList<String> extractedorderedIds = new ArrayList<>();
        for (ClientDice dice : clientModel.getExtractedDices()) {
            extractedorderedIds.add(String.valueOf(dice.getDiceID()));
        }

        ArrayList<String> extractedDicesImageViewIds = new ArrayList<>();
        for (Node dice : extractedDicesGrid.getChildren()) {
            extractedDicesImageViewIds.add(dice.getId());
        }

        if (extractedDicesGrid.getChildren().size() > extractedorderedIds.size()) {

            for (int i = 0; i < extractedDicesGrid.getChildren().size(); i++) {
                if (!extractedorderedIds.contains(extractedDicesGrid.getChildren().get(i).getId())) {
                    for(ImageView image: extractDices){
                        if(image.getId().equals(String.valueOf(extractedDicesGrid.getChildren().get(i).getId())))
                            extractDices.remove(image);
                    }
                    extractedDicesGrid.getChildren().remove(i);
                }
            }

        } else {
            int lenght = extractedDicesGrid.getChildren().size();
            for (int i = 0; i < lenght; i++) {
                tempIndex = extractedorderedIds.indexOf(extractedDicesGrid.getChildren().get(0).getId());
                System.out.println("id: " +tempIndex);
                tempNode = extractedDicesGrid.getChildren().get(0);
                tempPosition= extractedDicesGrid.getRowIndex(tempNode);
                if (tempIndex != -1) {
                    for(ImageView image: extractDices){
                        if(image.getId().equals(String.valueOf(tempNode.getId())))
                            extractDices.remove(image);
                    }
                    extractedDicesGrid.getChildren().remove(0);
                    ImageView dice = setDiceStyle(clientModel.getExtractedDices().get(tempIndex));
                    dice.setFitHeight(100);
                    dice.setFitWidth(100);
                    extractDices.add(dice);
                    extractedDicesGrid.add(dice, 0, tempPosition);
                } else {
                    for (int j = 0; j < extractedorderedIds.size(); j++) {
                        if (!extractedDicesImageViewIds.contains(extractedorderedIds.get(j)))
                            newDiceIndex = j;
                    }
                    if (newDiceIndex != -1) {
                        for(ImageView image: extractDices){
                            if(image.getId().equals(String.valueOf(tempNode.getId())))
                                extractDices.remove(image);
                        }
                        extractedDicesGrid.getChildren().remove(0);
                        ImageView dice = setDiceStyle(clientModel.getExtractedDices().get(newDiceIndex));
                        dice.setFitHeight(100);
                        dice.setFitWidth(100);
                        extractDices.add(dice);
                        extractedDicesGrid.add(dice, 0, tempPosition);
                    }
                }

            }
        }
    }

    /**
     * Sets all the dices inside the Round Track.
     */
    private synchronized void updateGraphicRoundTrack() {
        ClientRoundTrack roundTrack = clientModel.getRoundTrack();
        roundTrackDices.clear();
        roundTrackGrid.getChildren().clear();
        ClientDice[][] dice = roundTrack.getAllDices();
        for (int row = 0; row < NUM_OF_ROUNDS; row++) {
            for (int column = 0; column < NUM_OF_ROUNDS; column++) {
                if (dice[column][row] != null) {
                    ImageView image = setDiceStyle(dice[column][row]);
                    image.setFitHeight(30);
                    image.setFitWidth(30);
                    roundTrackGrid.add(image, column, row);
                    roundTrackDices.add(image);
                }
            }
        }
    }

    /**
     * Calls the filler of the schema.
     */
    private synchronized void updateGraphicMyWpc() { fillWpc(firstWpcGrid, clientModel.getMyWpc()); }

    /**
     * Updates all the graphic elements.
     */
    private synchronized void updateGraphicsCurrentUser() {
        updateGraphicExtractedDices();
        updateGraphicMyWpc();
        updateGraphicRoundTrack();
    }


    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
    }


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
        nextRound();
    }

    @Override
    public void handle(NextTurnNotification notification) {
        System.out.println("Turno: " + notification.turnNumber + "\tRound: " + clientModel.getCurrentRound());
        System.out.println("utente Attivo " + notification.activeUser);
        synchronized (waiter) {
            waiter.notify();
        }
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
    }

    @Override
    public void handle(DicePlacedNotification notification) {
        Platform.runLater(() -> {
            String user = notification.username;
            String id = String.valueOf(notification.dice.getDiceID());
            if (user.equals(secondUserLabel.getText()))
                fillWpc(secondWpcGrid, notification.wpc);
            else if ( user.equals(thirdUserLabel.getText()) && thirdUserLabel.getText()!= null)
                fillWpc(thirdWpcGrid, notification.wpc);
            else if ( user.equals(fourthUserLabel.getText()) && fourthUserLabel.getText()!= null)
                fillWpc(fourthWpcGrid, notification.wpc);
            for(int i= 0; i< extractedDicesGrid.getChildren().size(); i++) {
                if (extractedDicesGrid.getChildren().get(i).getId().equals(id))
                    extractedDicesGrid.getChildren().remove(i);
            }
        });
    }


    @Override
    public void handle(ToolCardUsedNotification notification) {
        Platform.runLater(() -> {
            if(notification.toolCard.getId().equals(toolCardsIDs.get(0).getId()))
                usedTool1Icon.setVisible(true);
            else if(notification.toolCard.getId().equals(toolCardsIDs.get(1).getId()))
                usedTool2Icon.setVisible(true);
            else if(notification.toolCard.getId().equals(toolCardsIDs.get(2).getId()))
                usedTool3Icon.setVisible(true);

            firstFavourLabel.setText(String.valueOf(clientModel.getFavour())+ "X");
            if (notification.username.equals(secondUserLabel.getText())) {
                fillWpc(secondWpcGrid, clientModel.getWpcByUsername().get(notification.username));
                updateGraphicExtractedDices();
            }
            else if ( thirdUserLabel != null && notification.username.equals(thirdUserLabel.getText())){
                fillWpc(thirdWpcGrid, clientModel.getWpcByUsername().get(notification.username));
                updateGraphicExtractedDices();
            }
            else if ( fourthUserLabel != null && notification.username.equals(fourthUserLabel.getText())){
                fillWpc(fourthWpcGrid, clientModel.getWpcByUsername().get(notification.username));
                updateGraphicExtractedDices();
            }
            if (!notification.username.equals(username)) {
                for (Notification not : notification.movesNotifications) not.handle(this);
            }
        });
    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {
    }

    @Override
    public void handle(ScoreNotification notification) {
        ArrayList<Map.Entry<String, Integer>> scores = new ArrayList<>(notification.scoreList.entrySet());
        scores.sort((Comparator<Map.Entry<?, Integer>>) (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        setScore(scores);
    }

    @Override
    public void handle(ToolCardDicePlacedNotification toolCardDicePlacedNotification) {
        Platform.runLater(() -> {
            String user = toolCardDicePlacedNotification.username;
            String id = String.valueOf(toolCardDicePlacedNotification.dice.getDiceID());
            if (user.equals(secondUserLabel.getText())) updateGraphicRoundTrack();
            else if (user.equals(thirdUserLabel.getText())){
                fillWpc(thirdWpcGrid, clientModel.getWpcByUsername().get(user));
                updateGraphicRoundTrack();
            }
            for(int i= 0; i< extractedDicesGrid.getChildren().size(); i++) {
                if (extractedDicesGrid.getChildren().get(i).getId().equals(id))
                    extractedDicesGrid.getChildren().remove(i);
            }

            //TODO: scrivi la mossa fatta
        });
    }


    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification toolCardExtractedDicesModifiedNotification) {
        Platform.runLater(() -> {
            //TODO: scrivi la mossa fatta
        });
    }
}