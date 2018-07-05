package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the roundTrack object in the server model, it doesn't contains any logic
 */
public class ClientRoundTrack implements Serializable {
    private final int currentRound;
    private final ClientDice[][] dicesNotUsed;

    /**
     * @param currentRound is the current round
     * @param dicesNotUsed is the matrix that contains all dices not used
     */
    public ClientRoundTrack(int currentRound, ClientDice[][] dicesNotUsed) {
        this.currentRound = currentRound;
        this.dicesNotUsed = dicesNotUsed;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public ClientDice[][] getAllDices(){
        return dicesNotUsed;
    }
}
