package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;

public interface Notification {

    public void handle(NotificationHandler handler);

}
