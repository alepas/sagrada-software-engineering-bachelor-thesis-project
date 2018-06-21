package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;

import java.util.ArrayList;

public class NewRoundNotification implements Notification {
    public final int roundNumber;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientWpc oldUserForcedWpc;
    public final String oldUserNameForced;

    public NewRoundNotification(int roundNumber, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientWpc oldUserForcedWpc, String oldUserNameForced) {
        this.roundNumber = roundNumber;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.oldUserForcedWpc = oldUserForcedWpc;
        this.oldUserNameForced = oldUserNameForced;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
