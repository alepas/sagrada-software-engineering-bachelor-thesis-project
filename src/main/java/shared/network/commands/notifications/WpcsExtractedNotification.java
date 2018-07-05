package shared.network.commands.notifications;

import shared.clientinfo.ClientWpc;

import java.util.ArrayList;
import java.util.HashMap;

public class WpcsExtractedNotification implements Notification {
    public String username;
    public final HashMap<String, ArrayList<ClientWpc>> wpcsByUser;
    public final int timeToCompleteTask;

    /**
     * @param wpcsByUser is the HashMap with all the extracted wpc for each player
     * @param timeToCompleteTask is how much time all players have for the schema choice
     */
    public WpcsExtractedNotification(HashMap<String, ArrayList<ClientWpc>> wpcsByUser, int timeToCompleteTask) {
        this.wpcsByUser = wpcsByUser;
        this.timeToCompleteTask = timeToCompleteTask;
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
