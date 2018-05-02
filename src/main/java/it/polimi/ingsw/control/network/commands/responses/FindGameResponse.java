package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class FindGameResponse implements Response {
    public final String gameID;
    public final String error;

    public FindGameResponse(String gameID, String error) {
        this.gameID = gameID;
        this.error = error;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}