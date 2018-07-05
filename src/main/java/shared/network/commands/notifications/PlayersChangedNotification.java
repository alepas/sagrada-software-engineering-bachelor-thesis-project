package shared.network.commands.notifications;

public class PlayersChangedNotification implements Notification {
    public final String username;
    public final boolean joined;
    public final int actualPlayers;
    public final int numPlayers;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     * @param joined is true if the player has joined the game, false if is abandoning it
     * @param actualPlayers is the number of players that have already joined the game
     * @param numPlayers is the max bound of players in the game
     */
    public PlayersChangedNotification(String username, boolean joined, int actualPlayers, int numPlayers) {
        this.username = username;
        this.joined = joined;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
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
