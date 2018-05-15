package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class UserPickedWpcNotification implements Response, Notification {
    public final String username;
    public final String wpcID;

    public UserPickedWpcNotification(String username, String wpcID) {
        this.username = username;
        this.wpcID = wpcID;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
