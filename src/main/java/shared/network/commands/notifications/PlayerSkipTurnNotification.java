package shared.network.commands.notifications;

public class PlayerSkipTurnNotification implements Notification {
    public final String username;
    public final String cardId;
    public final boolean disconnected;

    /**
     * @param username is the player's username
     * @param cardId is the card's id
     * @param disconnected is true if the player is disconnected
     */
    public PlayerSkipTurnNotification(String username, String cardId, boolean disconnected) {
        this.username = username;
        this.cardId = cardId;
        this.disconnected = true;
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
