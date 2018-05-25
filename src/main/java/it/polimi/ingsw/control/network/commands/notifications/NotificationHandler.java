package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.notifications.*;

public interface NotificationHandler {
    void handle(GameStartedNotification notification);

    void handle(PlayersChangedNotification notification);

    void handle(PrivateObjExtractedNotification notification);

    void handle(WpcsExtractedNotification notification);

    void handle(UserPickedWpcNotification notification);

    void handle(ToolcardsExtractedNotification notification);

    void handle(PocsExtractedNotification notification);

    void handle(NewRoundNotification notification);

    void handle(NextTurnNotification notification);

    void handle(DiceChangedNotification notification);

    void handle(DicePlacedNotification notification);

    void handle(ToolCardCanceledNotification notification);

    void handle(ToolCardUsedNotification notification);

    void handle(PlayerSkipTurnNotification notification);



}
