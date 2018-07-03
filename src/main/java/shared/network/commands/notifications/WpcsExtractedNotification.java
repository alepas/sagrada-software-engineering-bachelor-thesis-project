package shared.network.commands.notifications;

import shared.clientInfo.ClientWpc;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Notification {
    public String username;
    public final HashMap<String, ArrayList<ClientWpc>> wpcsByUser;
    public final int timeToCompleteTask;

    public WpcsExtractedNotification(HashMap<String, ArrayList<ClientWpc>> wpcsByUser, int timeToCompleteTask) {
        this.wpcsByUser = wpcsByUser;
        this.timeToCompleteTask = timeToCompleteTask;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
