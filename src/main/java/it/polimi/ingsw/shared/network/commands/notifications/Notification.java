package it.polimi.ingsw.shared.network.commands.notifications;

import java.io.Serializable;

public interface Notification extends Serializable {

    public void handle(NotificationHandler handler);

}
