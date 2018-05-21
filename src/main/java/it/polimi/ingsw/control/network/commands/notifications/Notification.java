package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;

import java.io.Serializable;

public interface Notification extends Serializable {

    public void handle(NotificationHandler handler);

}
