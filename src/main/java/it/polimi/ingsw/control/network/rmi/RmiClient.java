package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.PickWpcResponse;
import it.polimi.ingsw.control.network.commands.responses.notifications.*;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.constants.NetworkConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observer;

public class RmiClient extends NetworkClient implements ResponseHandler {
    private final RemoteServer remoteServer;

    private ClientModel clientModel = ClientModel.getInstance();
    private Observer observer = ClientModel.getInstance();

    public RmiClient() throws NotBoundException, RemoteException{
        Registry registry = LocateRegistry.getRegistry(NetworkConstants.SERVER_ADDRESS, Registry.REGISTRY_PORT);
        remoteServer = (RemoteServer) registry.lookup(NetworkConstants.RMI_CONTROLLER_NAME);
    }



    //-------------------------------- NetworkClientMethods --------------------------------

    @Override
    public void createUser(String username, String password) throws CannotRegisterUserException {
        try {
            ((CreateUserResponse) remoteServer.createUser(username, password)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        try {
            ((LoginResponse) remoteServer.login(username, password)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void findGame(String token, int numPlayers) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        try {
            RemoteObserver remoteObserver = new RmiRemoteObserver(this);
            ((FindGameResponse) remoteServer.findGame(token, numPlayers, remoteObserver)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        try {
            ((PickWpcResponse) remoteServer.pickWpc(userToken, wpcID)).handle(this);
        } catch (RemoteException e){

        }
    }


    //-------------------------------- Response Handler --------------------------------

    @Override
    public void handle(CreateUserResponse response) {
        if(response.userToken == null){
            clientModel.clean();
//            view.displayText(response.exception);         //TODO: fare exception
            return;
        }
        clientModel.setUsername(response.username);
        clientModel.setUserToken(response.userToken);
    }

    @Override
    public void handle(LoginResponse response) {
        if(response.userToken == null){
            clientModel.clean();
//            view.displayText(response.exception);
            return;
        }
        clientModel.setUsername(response.username);
        clientModel.setUserToken(response.userToken);
    }

    @Override
    public void handle(FindGameResponse response) {
        String gameID = response.gameID;
        if (gameID != null) {
            clientModel.setGameID(gameID);
            clientModel.setGameActualPlayers(response.actualPlayers);
            clientModel.setGameNumPlayers(response.numPlayers);
//            startReceiving();
        } else {
//            view.displayText(response.exception);
        }
    }

    @Override
    public void handle(PickWpcResponse response) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        Exception e = response.exception;
        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof NotYourWpcException) throw (NotYourWpcException) e;
        }
    }


    //------------------------------- Notification Handler ------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        observer.update(null, notification);
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        observer.update(null, notification);
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        notification.username = clientModel.getUsername();
        observer.update(null, notification);
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        notification.username = clientModel.getUsername();
        observer.update(null, notification);
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        clientModel.wpcByUsername.put(notification.username, notification.wpcID);
        observer.update(null, notification);
    }
}
