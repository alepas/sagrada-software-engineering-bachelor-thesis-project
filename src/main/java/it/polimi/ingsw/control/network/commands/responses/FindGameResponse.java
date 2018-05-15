package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;

public class FindGameResponse implements Response {
    public final String gameID;
    public final int actualPlayers;
    public final int numPlayers;
    public final Exception exception;

    public FindGameResponse(String gameID, int actualPlayers, int numPlayers, Exception exception) {
        this.gameID = gameID;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException {
        handler.handle(this);
    }
}