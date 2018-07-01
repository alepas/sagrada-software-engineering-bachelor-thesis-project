package shared.network.commands.notifications;

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

    void handle(ToolCardDiceChangedNotification notification);

    void handle(DicePlacedNotification notification);

    void handle(ToolCardUsedNotification notification);

    void handle(PlayerSkipTurnNotification notification);

    void handle(ScoreNotification notification);

    void handle(ToolCardDicePlacedNotification notification);

    void handle(ToolCardExtractedDicesModifiedNotification notification);

    void handle(PlayerDisconnectedNotification notification);

    void handle(PlayerReconnectedNotification notification);

    void handle(ForceDisconnectionNotification notification);

    void handle(ForceStartGameNotification notification);
}
