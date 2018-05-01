package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class GenericErrorResponse implements Response {
    public final String error;

    public GenericErrorResponse(String error) {
        this.error = error;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}