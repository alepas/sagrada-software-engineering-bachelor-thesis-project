package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;

import java.util.ArrayList;

public class PocsExtractedNotification implements Notification {
    public final ArrayList<String> pocIDs;

    public PocsExtractedNotification(ArrayList<String> pocIDs) {
        this.pocIDs = pocIDs;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
