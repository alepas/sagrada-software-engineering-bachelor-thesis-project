package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Response, Notification {
    public String username;
    public final HashMap<String, ArrayList<String>> wpcsByUser;

    public WpcsExtractedNotification(HashMap<String, ArrayList<String>> wpcsByUser) {
        this.wpcsByUser = wpcsByUser;
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
