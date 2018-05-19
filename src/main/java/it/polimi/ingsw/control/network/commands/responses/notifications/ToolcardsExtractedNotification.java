package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Response, Notification {
    public final ArrayList<String> toolcardsIDs;

    public ToolcardsExtractedNotification(ArrayList<String> toolcardsIDs) {
        this.toolcardsIDs = toolcardsIDs;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
