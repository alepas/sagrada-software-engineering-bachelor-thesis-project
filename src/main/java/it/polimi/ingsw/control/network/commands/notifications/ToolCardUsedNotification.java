package it.polimi.ingsw.control.network.commands.notifications;

import java.util.ArrayList;

public class ToolCardUsedNotification implements Notification {
    public final String username;
    public final String cardId;
    public final ArrayList<Notification> movesNotifications;

    public ToolCardUsedNotification(String username, String cardId, ArrayList<Notification> movesNotifications) {
        this.username = username;
        this.cardId = cardId;
        this.movesNotifications = movesNotifications;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
