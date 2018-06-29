package shared.network.commands.notifications;

import java.util.HashMap;

public class ScoreNotification implements Notification {
    public final HashMap<String, Integer> scoreList;

    public ScoreNotification(HashMap<String, Integer> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
