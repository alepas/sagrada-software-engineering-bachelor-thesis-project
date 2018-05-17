package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.constants.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Response, Notification {
    public String username;
    public final HashMap<String, ArrayList<String>> wpcsByUser;
    public final int timeToCompleteTask = GameConstants.CHOOSE_WPC_WAITING_TIME;

    public WpcsExtractedNotification(HashMap<String, ArrayList<String>> wpcsByUser) {
        this.wpcsByUser = wpcsByUser;
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
