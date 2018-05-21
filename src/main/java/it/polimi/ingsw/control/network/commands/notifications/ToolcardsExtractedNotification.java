package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<String> toolcardsIDs;

    public ToolcardsExtractedNotification(ArrayList<String> toolcardsIDs) {
        this.toolcardsIDs = toolcardsIDs;
    }


    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
