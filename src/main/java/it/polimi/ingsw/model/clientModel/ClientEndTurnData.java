package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientEndTurnData implements Serializable {
    public final String oldUser;
    public final ClientWpc wpcOldUser;
    public final ClientToolCard toolCardUsed;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;

    public ClientEndTurnData(String oldUser, ClientWpc wpcOldUser, ClientToolCard toolCardUsed, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack) {
        this.oldUser = oldUser;
        this.wpcOldUser = wpcOldUser;
        this.toolCardUsed = toolCardUsed;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
    }

}