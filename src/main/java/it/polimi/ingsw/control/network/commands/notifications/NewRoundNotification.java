package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.dicebag.Dice;

import java.util.ArrayList;

public class NewRoundNotification implements Notification {
    public final int roundNumber;
    public final ArrayList<Dice> extractedDices;

    public NewRoundNotification(int roundNumber, ArrayList<Dice> extractedDices) {
        this.roundNumber = roundNumber;
        this.extractedDices = extractedDices;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
