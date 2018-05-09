package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.dicebag.Color;

import java.util.HashMap;

public class PrivateObjExtractedNotification implements Response, Notification {
    public final HashMap<String, Color[]> colorsByUser;
    public String username;

    public PrivateObjExtractedNotification(HashMap<String, Color[]> colorsByUser) {
        this.colorsByUser = colorsByUser;
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
