package shared.clientinfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used by toolcards, it doesn't contains any logic
 */
public class ClientEndTurnData implements Serializable {
    public final String oldUser;
    public final ClientWpc wpcOldUser;
    public final ClientToolCard toolCardUsed;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;

    /**
     * @param oldUser is the old player's username
     * @param wpcOldUser is the player's schema
     * @param toolCardUsed is the used toolcard
     * @param extractedDices is the arraylist formed by all the extracted dices
     * @param roundTrack is th ematrix containing all the dices not used
     */
    public ClientEndTurnData(String oldUser, ClientWpc wpcOldUser, ClientToolCard toolCardUsed, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack) {
        this.oldUser = oldUser;
        this.wpcOldUser = wpcOldUser;
        this.toolCardUsed = toolCardUsed;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
    }

}