package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.constants.NetworkConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observer;

public class RmiClient implements NetworkClient, ResponseHandler {
    private final RemoteServer remoteServer;

    private ClientModel clientModel = ClientModel.getInstance();
    private Observer observer = ClientModel.getInstance();

    public RmiClient() throws NotBoundException, RemoteException{
        Registry registry = LocateRegistry.getRegistry(NetworkConstants.SERVER_ADDRESS, Registry.REGISTRY_PORT);
        remoteServer = (RemoteServer) registry.lookup(NetworkConstants.RMI_CONTROLLER_NAME);
    }

    @Override
    public void startPlaying() {
        try {
            RemoteObserver remoteObserver = new RmiRemoteObserver(this);
            remoteServer.addObserver(remoteObserver, clientModel.getGameID());
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

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
    public void findGame(String token, int numPlayers) throws CannotFindUserInDBException, InvalidPlayersException, NullTokenException {
        try {
            ((FindGameResponse) remoteServer.findGame(token, numPlayers)).handle(this);
        } catch (RemoteException e){

        }
    }

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
            startPlaying();
        } else {
//            view.displayText(response.exception);
        }
    }

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
        ((PrivateObjExtractedNotification) notification).username = clientModel.getUsername();
        observer.update(null, notification);
    }
}
