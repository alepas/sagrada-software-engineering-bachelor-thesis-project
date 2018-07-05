package shared.network.commands.notifications;

import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientDiceLocations;

public class ToolCardDiceChangedNotification implements Notification {
    public final String username;
    public final ClientDice oldDice;
    public final ClientDice newDice;
    public final ClientDiceLocations oldPosition;
    public final ClientDiceLocations newPosition;


    /**
     * Constructor of this.
     *
     * @param username is the player's username
     * @param oldDice is the oldDice
     * @param newDice is the newDice that will replace the oldDice
     * @param oldPosition is the old postion of the old dice
     * @param newPosition is the new position of the dice
     */
    public ToolCardDiceChangedNotification(String username, ClientDice oldDice, ClientDice newDice, ClientDiceLocations oldPosition, ClientDiceLocations newPosition) {
        this.username = username;
        this.oldDice = oldDice;
        this.newDice = newDice;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
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
