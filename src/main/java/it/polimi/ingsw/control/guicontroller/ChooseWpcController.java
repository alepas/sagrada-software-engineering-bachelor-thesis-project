/*
package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.Notification;
import it.polimi.ingsw.model.clientModel.ClientCell;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static it.polimi.ingsw.model.clientModel.ClientWpcConstants.setWpcName;
import static java.lang.String.valueOf;

public class ChooseWpcController implements Observer, {

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

    private String wpc0ID;
    private String wpc1ID;
    private String wpc2ID;
    private String wpc3ID;

    private NetworkClient networkClient;
    private ClientModel clientModel;

    @FXML
    void initialize(){
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();
        clientModel.addObserver(this);

        firstWPC.setOnMouseClicked(event -> pickWpc(event, wpc0ID));

        secondWPC.setOnMouseClicked(event -> pickWpc(event, wpc1ID));

        thirdWPC.setOnMouseClicked(event -> pickWpc(event, wpc2ID));

        fourthWPC.setOnMouseClicked(event -> pickWpc(event, wpc3ID));
    }



    */
/**
     * waits that the four randoms schemas arrive; when the thread takes the lock it starts the setting machine of the
     * schemas.
     *//*

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
                            wpc0ID = wpc.getWpcID();
                            wpc0Name.setText(setWpcName(wpc.getWpcID()));
                            setWpc(firstWPC, wpc);
                            setFirstWpcFavours(wpc.getFavours());
                            break;
                        case 1:
                            wpc1ID = wpc.getWpcID();
                            wpc1Name.setText(setWpcName(wpc.getWpcID()));
                            setWpc(secondWPC, wpc);
                            setSecondWpcFavours(wpc.getFavours());
                            break;
                        case 2:
                            wpc2ID = wpc.getWpcID();
                            wpc2Name.setText(setWpcName(wpc.getWpcID()));
                            setWpc(thirdWPC, wpc);
                            setThirdWpcFavours(wpc.getFavours());
                            break;
                        case 3:
                            wpc3ID = wpc.getWpcID();
                            wpc3Name.setText(setWpcName(wpc.getWpcID()));
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



    */
/**
     * Sets visible as many circles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 4th schema.
     *//*

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


    */
/**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 3rd schema.
     *//*

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


    */
/**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 2nd schema.
     *//*

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

    */
/**
     * Sets visible as many cyrcles as many favours the schema has.
     *
     * @param favours is the numebr of favours of the 1st schema.
     *//*

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


    */
/**
     * Fills the grid with AnchorPanes filled with restrictions by calling the two methods fillNumber and fillColor.
     *
     * @param gridPane is the schema tat will be fill
     * @param wpc  is the schema which is going to tell how to fill the gridPane
     *//*

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


    */
/**
     * @param cell is the anchorPane that i have to fill
     * @param number is the number restriction
     *//*

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


    */
/**
     * @param cell is a cell
     * @param color is the filling color of a schema's cell
     *//*

    private void fillColor(AnchorPane cell, ClientColor color) {
        if (color != null)
            cell.getStyleClass().add(color.toString().toLowerCase());
        else cell.getStyleClass().add("white");
    }


    */
/**
     * Calls the network method that will assign the schema chosen to the player.
     *
     * @param event  is the event generated by the action of the user while he/she has chosen the wpc
     * @param wpcID  is the ID of the schema chosen by the player
     *//*

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




    */
/**
     * when all game parameters have been settled the window change scene; it depends on the actual players number.
     *
     * @param event is the event necessry to change scene
     *//*

    private void playGame(Event event) {
        Thread startGame = new Thread(() -> {
            while (!pocExtracted || !toolExtracted || !allWpcsPicked || !privateObjExtractd) {
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
                        changeSceneHandle(event, "/view/gui/SoloPlayerGameScene.fxml");
                        break;
                    case 2:
                        changeSceneHandle(event, "/view/gui/TwoPlayersGameScene.fxml");
                        break;
                    case 3:
                        changeSceneHandle(event, "/view/gui/ThreePlayersGameScene.fxml");
                        break;
                    case 4:
                        changeSceneHandle(event, "/view/gui/FourPlayersGameScene.fxml");
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
}
*/
