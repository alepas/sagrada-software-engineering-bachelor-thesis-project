package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientPosition;

public class PickPositionRequest implements Request {
    public final ClientPosition position;
    public final String userToken;

    public PickPositionRequest(String userToken, ClientPosition position) {
        this.position = position;
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
