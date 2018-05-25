package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;

public class PlayerSkipTurnNotification implements Notification {
    public final String username;
    public final String cardId;

    public PlayerSkipTurnNotification(String username, String cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
