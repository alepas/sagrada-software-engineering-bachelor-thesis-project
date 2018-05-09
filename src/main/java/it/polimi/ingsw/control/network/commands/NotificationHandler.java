package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;

public interface NotificationHandler {
    void handle(GameStartedNotification notification);

    void handle(PlayersChangedNotification notification);

    void handle(PrivateObjExtractedNotification notification);
}
