package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.shared.clientInfo.ClientDice;
import it.polimi.ingsw.shared.clientInfo.ClientRoundTrack;
import it.polimi.ingsw.shared.clientInfo.ClientWpc;
import it.polimi.ingsw.shared.clientInfo.Position;

import java.util.ArrayList;

public class DicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final Position position;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;

    public DicePlacedNotification(String username, ClientDice dice, Position position, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack) {
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
