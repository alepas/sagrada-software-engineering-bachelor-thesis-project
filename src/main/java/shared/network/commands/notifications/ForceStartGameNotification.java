package shared.network.commands.notifications;

import shared.clientinfo.ClientGame;

/**
 * When a multi-player game should start but the number of players is equals to one the game will be forced to start and
 * then will immediately ends
 */
public class ForceStartGameNotification implements Notification {
    public final ClientGame game;

    /**
     * Constructor of this
     *
     * @param game is the game that must be froced to start
     */
    public ForceStartGameNotification(ClientGame game) {
        this.game = game;
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
