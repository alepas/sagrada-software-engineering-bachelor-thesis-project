package shared.network.commands.notifications;



public class ToolCardExtractedDicesModifiedNotification implements Notification {
    public final String username;

    /**
     * Constructor of this.
     *
     * @param username is the player username
     */
    public ToolCardExtractedDicesModifiedNotification(String username) {
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
