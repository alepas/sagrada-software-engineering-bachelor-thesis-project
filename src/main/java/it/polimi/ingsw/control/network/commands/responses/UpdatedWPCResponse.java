package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientWpc;

public class UpdatedWPCResponse implements Response {
    public final String user;
    public final ClientWpc wpc;
    public final Exception exception;

    public UpdatedWPCResponse(String user, ClientWpc wpc) {
        this.user = user;
        this.wpc = wpc;
        this.exception = null;
    }

    public UpdatedWPCResponse(Exception exception) {
        this.exception = exception;
        this.user=null;
        this.wpc=null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}