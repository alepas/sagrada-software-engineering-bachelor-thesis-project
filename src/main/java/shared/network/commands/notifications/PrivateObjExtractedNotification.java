package shared.network.commands.notifications;

import shared.clientinfo.ClientColor;

import java.util.HashMap;

public class PrivateObjExtractedNotification implements Notification {
    public final HashMap<String, ClientColor[]> colorsByUser;

    /**
     * Constructor of this.
     *
     * @param colorsByUser is the HashMap that associated to each player a private color
     */
    public PrivateObjExtractedNotification(HashMap<String, ClientColor[]> colorsByUser) {
        this.colorsByUser = colorsByUser;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
