package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.cards.ToolCardDB;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<String> ids;

    public ToolcardsExtractedNotification(ArrayList<String> ids) {
        this.ids = ids;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
