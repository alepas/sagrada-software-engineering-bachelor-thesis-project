package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;

import java.util.ArrayList;

public class DicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final ClientPosition position;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;

    public DicePlacedNotification(String username, ClientDice dice, ClientPosition position, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack) {
        this.username = username;
        this.dice = dice;
        this.position = position;
        this.wpc = wpc;
        this.newExtractedDices = newExtractedDices;
        this.newRoundTrack = newRoundTrack;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
