package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.util.*;

public class TwoPlayersGameController implements Observer, NotificationHandler {


    @FXML private GridPane extractedDicesGrid;
    private NetworkClient networkClient;
    private ClientModel clientModel;
    //TODO private ClientColor[] privateObjective;
    private ArrayList<ClientToolCard> toolCardsIDs = new ArrayList<>();
    private ArrayList<ClientPoc> pocIDs = new ArrayList<>();
    private String round;
    private ArrayList<ImageView> dices = new ArrayList<>();

    @FXML private GridPane firstWpcGrid;

    @FXML private GridPane pocGrid;

    @FXML private GridPane privateObjectiveGrid;

    @FXML private  GridPane secondWpcGrid;

    @FXML private GridPane toolCardGrid;

    @FXML private Label firstUserLabel;

    @FXML private Label roundLabel;

    @FXML private Label secondUserLabel;

    @FXML private MenuItem toolCardButton;

    @FXML private MenuItem roundTrackButton;

    @FXML private MenuItem pocButton;

    @FXML private MenuItem privateObjectiveButton;
    private ArrayList<AnchorPane> schema = new ArrayList<>();


    @FXML
    void initialize(){
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        String username = clientModel.getUsername();

        round = String.valueOf(clientModel.getCurrentRound());
        if(clientModel.isActive())
            roundLabel.setText("Round numero "+ round +",player "+ username);

        toolCardsIDs = clientModel.getGameToolCards();
        for(int tool = 0; tool < toolCardsIDs.size(); tool++) {
            String id = toolCardsIDs.get(tool).getId();
            setToolCard( id , tool);
        }

        pocIDs = clientModel.getGamePublicObjectiveCards();
        for(int poc = 0; poc < pocIDs.size(); poc++ ) {
            String id = pocIDs.get(poc).getId();
            setPoc( id , poc);
        }

        HashMap<String, ClientWpc> wpc = clientModel.getWpcByUsername();
        for(String wpcUser: wpc.keySet()){

            if(wpcUser.equals(username)) {
                firstUserLabel.setText(username);
                setWpc(firstWpcGrid, wpc.get(wpcUser));
            }
            else {
                secondUserLabel.setText(wpcUser);
                setWpc(secondWpcGrid, wpc.get(wpcUser));
            }
        }

        ArrayList<ClientDice> extractedDices = clientModel.getExtractedDices();
        for(int i = 0; i < extractedDices.size(); i++){
            ClientDice dice = extractedDices.get(i);
            setDice(dice, i);
        }

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

        if(!clientModel.isActive()){
            for(ImageView dice: dices) {
                dice.setDisable(true);
            }
        }

        for(ImageView dice: dices) {
            dice.setOnDragDetected(event -> {
                Dragboard db = dice.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(dice.getImage());
                dice.setVisible(false);
                db.setContent(content);
                event.consume();
            });

            dice.setOnDragDone(event -> {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    System.out.println("Drag finito");
                }
                event.consume();
            });
        }

        for(AnchorPane cell: schema) {
            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell)
                    event.acceptTransferModes(TransferMode.MOVE);

                event.consume();
            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    Image image = db.getImage();
                    ImageView dice = new ImageView(image);
                    dice.setFitWidth(70);
                    dice.setFitHeight(70);
                    cell.getChildren().add(dice);

                    System.out.println(db.getImage());

                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();
            });

        }

    }

    private void setDice(ClientDice dice, int row) {
        ClientColor color = dice.getDiceColor();
        String  number = String.valueOf(dice.getDiceNumber());
        String id = String.valueOf(dice.getDiceID());
        String style = null;
        switch (color) {
            case RED:
                style = "red";
                style = style.concat(number);
                break;
            case YELLOW:
                style = "yellow";
                style = style.concat(number);
                break;
            case GREEN:
                style = "green";
                style = style.concat(number);
                break;
            case VIOLET:
                style = "violet";
                style = style.concat(number);
                break;
            case BLUE:
                style = "blue";
                style = style.concat(number);
                break;
        }
        ImageView image = new ImageView();
        image.getStyleClass().add(style);
        image.setFitHeight(100);
        image.setFitWidth(100);
        dices.add(image);
        image.setId(id);
        extractedDicesGrid.add(image, 0, row);
    }


    private void setPrivateObjective(ClientColor color, int row) {
        AnchorPane privateObj = new AnchorPane();
        switch (color){
            case VIOLET:
                break;
            case BLUE:
                break;
            case GREEN:
                break;
            case YELLOW:
                break;
            case RED:
                break;
        }
        privateObjectiveGrid.add(privateObj, 0, row );
        privateObjectiveGrid.setVisible(false);
    }


    private void setPoc(String id, int row) {
        Platform.runLater(() -> {
            AnchorPane POC = new AnchorPane();
            switch (id) {
                case "1":
                    POC.setId("poc1");
                    break;
                case "2":
                    POC.setId("poc2");
                    break;
                case "3":
                    POC.setId("poc3");
                    break;
                case "4":
                    POC.setId("poc4");
                    break;
                case "5":
                    POC.setId("poc5");
                    break;
                case "6":
                    POC.setId("poc6");
                    break;
                case "7":
                    POC.setId("poc7");
                    break;
                case "8":
                    POC.setId("poc8");
                    break;
                case "9":
                    POC.setId("poc9");
                    break;
                case "10":
                    POC.setId("poc10");
                    break;
            }
            pocGrid.add(POC, 0, row);
            pocGrid.setVisible(true);
        });
    }


    private void setToolCard(String id, int row) {
        Platform.runLater(() -> {
            AnchorPane toolCard = new AnchorPane();
            switch (id) {
                case "1":
                    toolCard.setId("tool1");
                    break;
                case "2":
                    toolCard.setId("tool2");
                    break;
                case "3":
                    toolCard.setId("tool3");
                    break;
                case "4":
                    toolCard.setId("tool4");
                    break;
                case "5":
                    toolCard.setId("tool5");
                    break;
                case "6":
                    toolCard.setId("tool6");
                    break;
                case "7":
                    toolCard.setId("tool7");
                    break;
                case "8":
                    toolCard.setId("tool8");
                    break;
                case "9":
                    toolCard.setId("tool9");
                    break;
                case "10":
                    toolCard.setId("tool10");
                    break;
                case "11":
                    toolCard.setId("tool11");
                    break;
                case "12":
                    toolCard.setId("tool12");
                    break;
            }
            toolCardGrid.add(toolCard, 0, row);
            toolCardGrid.setVisible(false);
        });
    }


    private void setWpc(GridPane gridPane, ClientWpc wpc) {

        for (ClientCell cell : wpc.getSchema()) {
            int row = cell.getCellPosition().getRow();
            int column = cell.getCellPosition().getColumn();

            AnchorPane cellXY = new AnchorPane();
            ClientColor color = cell.getCellColor();
            fillColor(cellXY, color);

            int number = cell.getCellNumber();
            fillNumber(cellXY, number);
            cellXY.setId(String.valueOf(row)+String.valueOf(column));
            gridPane.add(cellXY, column, row);
            schema.add(cellXY);
        }
    }


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

    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) { ((Notification) arg).handle(this); }


    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {

    }

    @Override
    public void handle(PocsExtractedNotification notification) {

    }

    @Override
    public void handle(NewRoundNotification notification) {
        System.out.println("Round: " + notification.roundNumber);
        System.out.println("Dadi estratti: ");
        for (ClientDice dice : notification.extractedDices){
            System.out.println("ID: " + dice.getDiceID() + "\t" + dice.getDiceNumber() + " " + dice.getDiceColor());
        }

    }

    @Override
    public void handle(NextTurnNotification notification) {

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