package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class PickWpcRequest implements Request {
    public final String wpcID;
    public final String userToken;


    /**
     * Constructor of this
     *
     * @param wpcID is the id of the schema
     * @param userToken is the player's token
     */
    public PickWpcRequest(String wpcID, String userToken) {
        this.wpcID = wpcID;
        this.userToken = userToken;
    }


    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
