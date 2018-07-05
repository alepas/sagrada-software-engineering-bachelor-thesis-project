package shared.network.commands.notifications;

import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientRoundTrack;
import shared.clientInfo.ClientWpc;
import shared.clientInfo.Position;

import java.util.ArrayList;

public class DicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final Position position;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> newExtractedDices;
    public final ClientRoundTrack newRoundTrack;

    /**
     * @param username is the player's name
     * @param dice is the chosen dice
     * @param position is the chosen position where the player would like to add the dice
     * @param wpc is the player's schema
     * @param newExtractedDices is the arrayList with all the extracted dices
     * @param newRoundTrack is the matrix with all the dices not used by the players
     */
    public DicePlacedNotification(String username, ClientDice dice, Position position, ClientWpc wpc, ArrayList<ClientDice> newExtractedDices, ClientRoundTrack newRoundTrack) {
        this.username = username;
        this.dice = dice;
        this.position = position;
        this.wpc = wpc;
        this.newExtractedDices = newExtractedDices;
        this.newRoundTrack = newRoundTrack;
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
