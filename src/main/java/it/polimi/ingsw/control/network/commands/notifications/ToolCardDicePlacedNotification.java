package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;

public class ToolCardDicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final Position position;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;

    public ToolCardDicePlacedNotification(String username, ClientDice dice, Position position, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack) {
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
