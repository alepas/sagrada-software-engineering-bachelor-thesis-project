package shared.network.commands.notifications;

import shared.clientinfo.ClientDice;
import shared.clientinfo.Position;

public class ToolCardDicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final Position position;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     * @param dice is the dice chosen by the user
     * @param position is the position where the player would like to add the dice
     */
    public ToolCardDicePlacedNotification(String username, ClientDice dice, Position position) {
        this.username = username;
        this.dice = dice;
        this.position = position;
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
