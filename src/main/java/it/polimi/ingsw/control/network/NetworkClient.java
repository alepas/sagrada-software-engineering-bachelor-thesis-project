package it.polimi.ingsw.control.network;

import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;

public interface NetworkClient {

    void startPlaying();

    void createUser(String username, String password) throws CannotRegisterUserException;

    void login(String username, String password) throws CannotLoginUserException;

    void findGame(String token, int numPlayers) throws InvalidPlayersException, NullTokenException, CannotFindUserInDBException;
}
