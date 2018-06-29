package it.polimi.ingsw.shared.clientInfo;

import java.io.Serializable;

public class ClientRoundTrack implements Serializable {
    private final int currentRound;
    private final ClientDice[][] dicesNotUsed;

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
