package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientUser;

public class GetUserStatResponse implements Response {
    public final ClientUser user;
    public final Exception exception;

    public GetUserStatResponse(ClientUser user, Exception exception) {
        this.user = user;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}
