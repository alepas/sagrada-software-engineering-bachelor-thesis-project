package shared.network.commands.notifications;

import java.io.Serializable;

public interface Notification extends Serializable {

    /**
     * that's the method called to handle all possible notification.
     *
     * @param handler is the notification handler
     */
    public void handle(NotificationHandler handler);

}
