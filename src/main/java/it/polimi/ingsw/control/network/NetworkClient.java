package it.polimi.ingsw.control.network;

import it.polimi.ingsw.control.network.rmi.RmiClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class NetworkClient {
    private static NetworkClient instance;

    public static SocketClient getNewSocketInstance(String host, int port){
        if (instance == null){
            instance = new SocketClient(host, port);
        }
        return (SocketClient) instance;
    }

    public static RmiClient getNewRmiInstance() throws NotBoundException, RemoteException {
        if (instance == null){
            instance = new RmiClient();
        }
        return (RmiClient) instance;
    }

    public static NetworkClient getInstance(){
        return instance;
    }

    public abstract void createUser(String username, String password) throws CannotRegisterUserException;

    public abstract void login(String username, String password) throws CannotLoginUserException;

    public abstract void findGame(String token, int numPlayers) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

    public abstract void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException;
}
