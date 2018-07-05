package shared.network.commands.notifications;

public class ForceDisconnectionNotification implements Notification {
    public final boolean lostConnection;

    /**
     * @param lostConnection is a true boolean if the connection has been lost, false if not
     */
    public ForceDisconnectionNotification(boolean lostConnection) {
        this.lostConnection = lostConnection;
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
