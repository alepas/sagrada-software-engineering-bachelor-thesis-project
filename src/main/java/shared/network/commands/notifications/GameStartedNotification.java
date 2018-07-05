package shared.network.commands.notifications;

/**
 * This object is created when the server has started the game
 */
public class GameStartedNotification implements Notification {

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
