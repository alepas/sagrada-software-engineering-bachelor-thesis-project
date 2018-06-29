package it.polimi.ingsw.shared.network.commands.notifications;



public class ToolCardExtractedDicesModifiedNotification implements Notification {
    public final String username;

    public ToolCardExtractedDicesModifiedNotification(String username) {
        this.username = username;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
