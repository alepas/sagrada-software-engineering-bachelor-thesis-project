package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.PickWpcResponse;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

public interface ResponseHandler extends NotificationHandler{

    void handle(CreateUserResponse response) throws CannotRegisterUserException;

    void handle(LoginResponse response) throws CannotLoginUserException;

    void handle(FindGameResponse response) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

    void handle(PickWpcResponse response) throws CannotFindPlayerInDatabaseException, NotYourWpcException;

}
