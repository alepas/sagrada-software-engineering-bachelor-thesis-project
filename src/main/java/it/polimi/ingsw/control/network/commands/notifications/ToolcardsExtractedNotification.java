package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientToolCard;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<ClientToolCard> cards;

    public ToolcardsExtractedNotification(ArrayList<ClientToolCard> cards) {
        this.cards = cards;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
