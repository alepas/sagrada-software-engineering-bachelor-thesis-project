package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientToolCard;
import it.polimi.ingsw.model.clientModel.ClientWpc;

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
