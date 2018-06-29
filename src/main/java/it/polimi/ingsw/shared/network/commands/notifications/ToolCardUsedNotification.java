package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.shared.clientInfo.ClientDice;
import it.polimi.ingsw.shared.clientInfo.ClientRoundTrack;
import it.polimi.ingsw.shared.clientInfo.ClientToolCard;
import it.polimi.ingsw.shared.clientInfo.ClientWpc;

import java.util.ArrayList;

public class ToolCardUsedNotification implements Notification {
    public final String username;
    public final ClientToolCard toolCard;
    public final ArrayList<Notification> movesNotifications;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;
    public final int favours;

    public ToolCardUsedNotification(String username, ClientToolCard toolCard, ArrayList<Notification> movesNotifications, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack, int favours) {
        this.username = username;
        this.toolCard = toolCard;
        this.movesNotifications = movesNotifications;
        this.wpc = wpc;
        this.newExtractedDices = newExtractedDices;
        this.newRoundTrack = newRoundTrack;
        this.favours = favours;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
