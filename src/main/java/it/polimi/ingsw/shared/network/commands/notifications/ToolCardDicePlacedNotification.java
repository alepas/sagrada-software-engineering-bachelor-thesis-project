package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.shared.clientInfo.ClientDice;
import it.polimi.ingsw.shared.clientInfo.Position;

public class ToolCardDicePlacedNotification implements Notification {
    public final String username;
    public final ClientDice dice;
    public final Position position;

    public ToolCardDicePlacedNotification(String username, ClientDice dice, Position position) {
        this.username = username;
        this.dice = dice;
        this.position = position;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
