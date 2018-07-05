package shared.network.commands.notifications;

import java.util.HashMap;

public class ScoreNotification implements Notification {
    public final HashMap<String, Integer> scoreList;

    /**
     * @param scoreList is the HashMaps which contains all scores
     */
    public ScoreNotification(HashMap<String, Integer> scoreList) {
        this.scoreList = scoreList;
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
