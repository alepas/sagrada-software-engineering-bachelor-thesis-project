package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response createUser(String username, String password) throws RemoteException, CannotRegisterUserException;

    Response login(String username, String password) throws RemoteException, CannotLoginUserException;

    Response findGame(String userToken, int numPlayers) throws RemoteException, CannotFindUserInDBException, InvalidPlayersException, NullTokenException;

    void addObserver(RemoteObserver observer, String gameID) throws RemoteException;
}
