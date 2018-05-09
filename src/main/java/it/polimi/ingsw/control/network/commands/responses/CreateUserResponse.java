package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;

public class CreateUserResponse implements Response {
    public final String username;
    public final String userToken;
    public final Exception exception;

    public CreateUserResponse(String username, String userToken, Exception exception) {
        this.username = username;
        this.userToken = userToken;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) throws CannotRegisterUserException {
        handler.handle(this);
    }
}
