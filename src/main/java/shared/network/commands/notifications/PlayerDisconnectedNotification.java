package shared.network.commands.notifications;

public class PlayerDisconnectedNotification implements Notification {
    public final String username;

    public PlayerDisconnectedNotification(String username) {
        this.username = username;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
