package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientPoc;

import java.util.ArrayList;

public class UpdatedPOCsResponse implements Response {
    public final ArrayList<ClientPoc> pocs;
    public final Exception exception;

    public UpdatedPOCsResponse(ArrayList<ClientPoc> pocs) {
        this.pocs = pocs;
        this.exception=null;

    }

    public UpdatedPOCsResponse(Exception exception) {
        this.exception = exception;
        this.pocs=null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}