package shared.network.commands.notifications;

import shared.clientInfo.ClientColor;

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
