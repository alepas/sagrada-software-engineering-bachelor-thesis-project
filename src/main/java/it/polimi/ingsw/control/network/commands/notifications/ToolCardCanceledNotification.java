package it.polimi.ingsw.control.network.commands.notifications;

public class ToolCardCanceledNotification implements Notification {
    public final String username;
    public final String cardId;

    public ToolCardCanceledNotification(String username, String cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
