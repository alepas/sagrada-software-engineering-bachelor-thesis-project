package shared.network.commands.responses;

/**
 * Contains all possible response handler methods
 */
public interface ResponseHandler{

    void handle(CreateUserResponse response);

    void handle(LoginResponse response);

    void handle(FindGameResponse response);

    void handle(PickWpcResponse response);

    void handle(PassTurnResponse response);

    void handle(ToolCardResponse response);

    void handle(UpdatedExtractedDicesResponse response);

    void handle(UpdatedGameResponse response);

    void handle(UpdatedPOCsResponse response);

    void handle(UpdatedRoundTrackResponse response);

    void handle(UpdatedToolCardsResponse response);

    void handle(UpdatedWPCResponse response);

    void handle(PlaceDiceResponse response);

    void handle(NextMoveResponse response);

    void handle(GetUserStatResponse response);
}
