package it.polimi.ingsw.shared.network.commands.notifications;

public class PlayerReconnectedNotification implements Notification {
    public final String username;

    public PlayerReconnectedNotification(String username) {
        this.username = username;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}