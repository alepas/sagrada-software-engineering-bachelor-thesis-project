package shared.network.commands.notifications;

import shared.clientInfo.ClientGame;

public class ForceStartGameNotification implements Notification {
    public final ClientGame game;

    public ForceStartGameNotification(ClientGame game) {
        this.game = game;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
