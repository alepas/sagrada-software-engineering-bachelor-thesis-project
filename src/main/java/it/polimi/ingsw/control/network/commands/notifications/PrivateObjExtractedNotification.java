package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.clientModel.ClientColor;

import java.util.HashMap;

public class PrivateObjExtractedNotification implements Notification {
    public final HashMap<String, ClientColor[]> colorsByUser;

    public PrivateObjExtractedNotification(HashMap<String, ClientColor[]> colorsByUser) {
        this.colorsByUser = colorsByUser;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
