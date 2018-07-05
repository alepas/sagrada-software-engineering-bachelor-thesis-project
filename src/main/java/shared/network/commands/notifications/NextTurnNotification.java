package shared.network.commands.notifications;

import shared.clientInfo.ClientEndTurnData;

public class NextTurnNotification implements Notification {
    public final int turnNumber;
    public final String activeUser;
    public final int timeToCompleteTask;
    public final ClientEndTurnData endTurnData;

    /**
     * @param turnNumber is the current turn number
     * @param activeUser is the username of the active player
     * @param timeToCompleteTask is an integer that express how much time those the player has to end his/her turn
     * @param endTurnData is the client end turn data for the turn
     */
    public NextTurnNotification(int turnNumber, String activeUser, int timeToCompleteTask, ClientEndTurnData endTurnData) {
        this.turnNumber = turnNumber;
        this.activeUser = activeUser;
        this.timeToCompleteTask = timeToCompleteTask;
        this.endTurnData = endTurnData;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
