package shared.network.commands.notifications;

public class PlayerReconnectedNotification implements Notification {
    public final String username;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     */
    public PlayerReconnectedNotification(String username) {
        this.username = username;
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
