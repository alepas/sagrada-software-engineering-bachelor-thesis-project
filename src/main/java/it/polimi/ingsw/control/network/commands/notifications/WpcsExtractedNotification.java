package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.constants.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Notification {
    public String username;
    public final HashMap<String, ArrayList<ClientWpc>> wpcsByUser;
    public final int timeToCompleteTask = GameConstants.CHOOSE_WPC_WAITING_TIME;

    public WpcsExtractedNotification(HashMap<String, ArrayList<ClientWpc>> wpcsByUser) {
        this.wpcsByUser = wpcsByUser;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
