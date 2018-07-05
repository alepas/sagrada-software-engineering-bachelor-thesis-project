package shared.network.commands.notifications;

import shared.clientinfo.ClientDice;
import shared.clientinfo.ClientRoundTrack;
import shared.clientinfo.ClientWpc;

import java.util.ArrayList;

public class NewRoundNotification implements Notification {
    public final int roundNumber;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientWpc oldUserForcedWpc;
    public final String oldUserNameForced;

    /**
     * @param roundNumber is the number of the current round
     * @param extractedDices is the arraylist formed by all the client dices extracted
     * @param roundTrack is the matrix formed with all unused dices
     * @param oldUserForcedWpc is the player's schema
     * @param oldUserNameForced is the player's schema name
     */
    public NewRoundNotification(int roundNumber, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientWpc oldUserForcedWpc, String oldUserNameForced) {
        this.roundNumber = roundNumber;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.oldUserForcedWpc = oldUserForcedWpc;
        this.oldUserNameForced = oldUserNameForced;
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
