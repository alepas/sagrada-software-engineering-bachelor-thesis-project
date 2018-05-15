package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;

public class PickWpcResponse implements Response {
    public final Exception exception;

    public PickWpcResponse(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        handler.handle(this);
    }
}
