package shared.network.commands.notifications;

public class GameStartedNotification implements Notification {

    public GameStartedNotification() { }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
