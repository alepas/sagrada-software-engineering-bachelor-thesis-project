package shared.network.commands.notifications;

import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientRoundTrack;
import shared.clientInfo.ClientToolCard;
import shared.clientInfo.ClientWpc;

import java.util.ArrayList;

public class ToolCardUsedNotification implements Notification {
    public final String username;
    public final ClientToolCard toolCard;
    public final ArrayList<Notification> movesNotifications;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;
    public final int favours;

    /**
     * Constructor of this.
     *
     * @param username is the player's username
     * @param toolCard is the toolcard what the player has used
     * @param movesNotifications is an arrayList which contains all the information related to the use of the toolcard
     * @param wpc is the player's schema
     * @param newExtractedDices is the arraylist formed by all the exctracted dices
     * @param newRoundTrack is the matrix formed by all dices non used during the game
     * @param favours is the number of favours of the player
     */
    public ToolCardUsedNotification(String username, ClientToolCard toolCard, ArrayList<Notification> movesNotifications, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack, int favours) {
        this.username = username;
        this.toolCard = toolCard;
        this.movesNotifications = movesNotifications;
        this.wpc = wpc;
        this.newExtractedDices = newExtractedDices;
        this.newRoundTrack = newRoundTrack;
        this.favours = favours;
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
