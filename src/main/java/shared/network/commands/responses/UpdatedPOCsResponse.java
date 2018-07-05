package shared.network.commands.responses;

import shared.clientinfo.ClientPoc;

import java.util.ArrayList;

public class UpdatedPOCsResponse implements Response {
    public final ArrayList<ClientPoc> pocs;
    public final Exception exception;

    /**
     * @param pocs is the arraylis composed by the public objective cards
     */
    public UpdatedPOCsResponse(ArrayList<ClientPoc> pocs) {
        this.pocs = pocs;
        this.exception=null;
    }

    /**
     * @param exception is the exception that could be thrown
     */
    public UpdatedPOCsResponse(Exception exception) {
        this.exception = exception;
        this.pocs=null;
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