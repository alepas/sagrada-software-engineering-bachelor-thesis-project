package shared.network.commands.responses;

import shared.clientinfo.ClientUser;

public class GetUserStatResponse implements Response {
    public final ClientUser user;
    public final Exception exception;

    /**
     * @param user is the client user related to the player
     * @param exception is the possible exception
     */
    public GetUserStatResponse(ClientUser user, Exception exception) {
        this.user = user;
        this.exception = exception;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}
