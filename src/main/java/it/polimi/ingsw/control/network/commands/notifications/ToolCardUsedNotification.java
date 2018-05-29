package it.polimi.ingsw.control.network.commands.notifications;

public class ToolCardUsedNotification implements Notification {
    public final String username;
    public final String cardId;

    public ToolCardUsedNotification(String username, String cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
