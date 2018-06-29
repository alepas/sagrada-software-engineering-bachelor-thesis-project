package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public interface RequestHandler {

    Response handle(CreateUserRequest request);

    Response handle(LoginRequest request);

    Response handle(FindGameRequest request);

    Response handle(PickWpcRequest request);

    Response handle(PassTurnRequest request);

    Response handle(ToolCardPickDiceRequest request);

    Response handle(ToolCardPickNumberRequest request);

    Response handle(ToolCardPlaceDiceRequest request);

    Response handle (ToolCardUseRequest request);

    Response handle(UpdatedRoundTrackRequest request);

    Response handle(UpdatedExtractedDicesRequest request);

    Response handle(UpdatedPOCsRequest request);

    Response handle(UpdatedToolCardsRequest request);

    Response handle(UpdatedWPCRequest request);

    Response handle(UpdatedGameRequest request);

    Response handle(ToolCardInteruptRequest toolCardInteruptRequest);

    Response handle(CancelActionRequest cancelActionRequest);

    Response handle(PlaceDiceRequest placeDiceRequest);

    Response handle(NextMoveRequest nextMoveRequest);

    Response handle(GetUserStatRequest request);

}
