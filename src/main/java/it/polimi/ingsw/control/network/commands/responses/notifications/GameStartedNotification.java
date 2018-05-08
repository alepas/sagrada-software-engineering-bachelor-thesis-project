package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class GameStartedNotification implements Response {

    public GameStartedNotification() { }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
