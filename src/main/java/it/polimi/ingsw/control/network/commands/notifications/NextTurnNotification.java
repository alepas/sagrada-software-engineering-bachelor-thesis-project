package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.constants.GameConstants;

public class NextTurnNotification implements Notification {
    public final int turnNumber;
    public final String activeUser;
    public final int timeToCompleteTask;

    public NextTurnNotification(int turnNumber, String activeUser) {
        this(turnNumber, activeUser, GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER);
    }

    public NextTurnNotification(int turnNumber, String activeUser, int timeToCompleteTask) {
        this.turnNumber = turnNumber;
        this.activeUser = activeUser;
        this.timeToCompleteTask = timeToCompleteTask;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
