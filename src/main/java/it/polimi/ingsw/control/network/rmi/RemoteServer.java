package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response createUser(String username, String password) throws RemoteException, CannotRegisterUserException;

    Response login(String username, String password) throws RemoteException, CannotLoginUserException;

    Response findGame(String userToken, int numPlayers, RemoteObserver observer) throws RemoteException, InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

    Response pickWpc(String userToken, String wpcID) throws RemoteException, CannotFindPlayerInDatabaseException, NotYourWpcException;
}
