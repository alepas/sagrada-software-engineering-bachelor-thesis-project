package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.server.constants.GameConstants;
import it.polimi.ingsw.shared.clientInfo.ClientEndTurnData;

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
