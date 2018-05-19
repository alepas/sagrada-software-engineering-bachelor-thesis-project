package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.notifications.*;

public interface NotificationHandler {
    void handle(GameStartedNotification notification);

    void handle(PlayersChangedNotification notification);

    void handle(PrivateObjExtractedNotification notification);

    void handle(WpcsExtractedNotification notification);

    void handle(UserPickedWpcNotification notification);

    void handle(ToolcardsExtractedNotification notification);

    void handle(PocsExtractedNotification notification);
}
