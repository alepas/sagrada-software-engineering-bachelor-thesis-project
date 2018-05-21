package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.dicebag.Color;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class FourPlayersGameController implements NotificationHandler {


    @FXML private GridPane firstWPC;

    @FXML private GridPane secondWPC;

    @FXML private GridPane thirdWPC;

    @FXML private GridPane fourthWPC;


    @Override
    public void handle(GameStartedNotification notification) {
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
    }

    @Override
    public void handle(PrivateObjExtractedNotification response) {
        Color[] colors = response.colorsByUser.get(response.username);
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (Color color : colors){
            str.append(color + "\t");
        }

        ///setText(str.toString());
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {

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
}
