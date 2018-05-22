package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.cards.PocDB;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

import java.util.ArrayList;

public class PocsExtractedNotification implements Notification {
    public final ArrayList<String> ids;

    public PocsExtractedNotification(ArrayList<String> ids) {
        this.ids = ids;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
