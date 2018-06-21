package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientEndTurnData;
import it.polimi.ingsw.model.constants.GameConstants;

public class NextTurnNotification implements Notification {
    public final int turnNumber;
    public final String activeUser;
    public final int timeToCompleteTask;
    public final ClientEndTurnData endTurnData;

    public NextTurnNotification(int turnNumber, String activeUser, ClientEndTurnData endTurnData) {
        this(turnNumber, activeUser, GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER, endTurnData);
    }

    public NextTurnNotification(int turnNumber, String activeUser, int timeToCompleteTask, ClientEndTurnData endTurnData) {
        this.turnNumber = turnNumber;
        this.activeUser = activeUser;
        this.timeToCompleteTask = timeToCompleteTask;
        this.endTurnData = endTurnData;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
