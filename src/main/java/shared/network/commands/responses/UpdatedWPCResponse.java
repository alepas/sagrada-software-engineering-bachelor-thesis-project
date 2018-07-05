package shared.network.commands.responses;

import shared.clientinfo.ClientWpc;

public class UpdatedWPCResponse implements Response {
    public final String user;
    public final ClientWpc wpc;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param user is the player's user
     * @param wpc is the player's schema
     */
    public UpdatedWPCResponse(String user, ClientWpc wpc) {
        this.user = user;
        this.wpc = wpc;
        this.exception = null;
    }

    /**
     * Constructor of this that sets everything to null except for the exception
     *
     * @param exception is the exception that could be thrown
     */
    public UpdatedWPCResponse(Exception exception) {
        this.exception = exception;
        this.user=null;
        this.wpc=null;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}