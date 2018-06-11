package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;

import java.util.ArrayList;

public class NewRoundNotification implements Notification {
    public final int roundNumber;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;

    public NewRoundNotification(int roundNumber, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack) {
        this.roundNumber = roundNumber;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
