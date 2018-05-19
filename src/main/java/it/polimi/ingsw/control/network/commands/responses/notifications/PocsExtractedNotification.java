package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

import java.util.ArrayList;

public class PocsExtractedNotification implements Response, Notification {
    public final ArrayList<String> pocIDs;

    public PocsExtractedNotification(ArrayList<String> pocIDs) {
        this.pocIDs = pocIDs;
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
