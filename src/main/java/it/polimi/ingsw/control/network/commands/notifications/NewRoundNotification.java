package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;

import java.util.ArrayList;

public class NewRoundNotification implements Notification {
    public final int roundNumber;
    public final ArrayList<ClientDice> extractedDices;

    public NewRoundNotification(int roundNumber, ArrayList<ClientDice> extractedDices) {
        this.roundNumber = roundNumber;
        this.extractedDices = extractedDices;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
