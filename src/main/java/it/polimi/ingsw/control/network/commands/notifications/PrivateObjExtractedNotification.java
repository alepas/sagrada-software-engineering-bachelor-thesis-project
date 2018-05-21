package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.dicebag.Color;

import java.util.HashMap;

public class PrivateObjExtractedNotification implements Notification {
    public final HashMap<String, Color[]> colorsByUser;
    public String username;

    public PrivateObjExtractedNotification(HashMap<String, Color[]> colorsByUser) {
        this.colorsByUser = colorsByUser;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
