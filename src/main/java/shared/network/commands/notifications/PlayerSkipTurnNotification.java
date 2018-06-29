package shared.network.commands.notifications;

public class PlayerSkipTurnNotification implements Notification {
    public final String username;
    public final String cardId;
    public final boolean disconnected;

    public PlayerSkipTurnNotification(String username, String cardId, boolean disconnected) {
        this.username = username;
        this.cardId = cardId;
        this.disconnected = true;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
