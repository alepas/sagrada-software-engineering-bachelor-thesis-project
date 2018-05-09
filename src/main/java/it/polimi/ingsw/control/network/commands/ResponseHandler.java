package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;

public interface ResponseHandler extends NotificationHandler{

    void handle(CreateUserResponse response) throws CannotRegisterUserException;

    void handle(LoginResponse response) throws CannotLoginUserException;

    void handle(FindGameResponse response) throws InvalidPlayersException, NullTokenException, CannotFindUserInDBException;

}
