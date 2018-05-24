package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;
import it.polimi.ingsw.model.constants.NetworkConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient extends NetworkClient {
    private final RemoteServer remoteServer;

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
            RemoteObserver remoteObserver = new RmiRemoteObserver();
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

    @Override
    public void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        try {
            remoteServer.passTurn(userToken).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException {
        try {
            ((ToolCardResponse) remoteServer.useToolCard(userToken, cardId)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException {
        try {
            ((ToolCardResponse) remoteServer.pickDiceForToolCard(userToken, diceId, where)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException {
        try {
            ((ToolCardResponse) remoteServer.pickPositionForToolCard(userToken, position)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException {
        try {
            ((ToolCardResponse) remoteServer.pickColorForToolCard(userToken, color)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException {
        try {
            ((ToolCardResponse) remoteServer.pickNumberForToolCard(userToken, number)).handle(this);
        } catch (RemoteException e){

        }
    }


    @Override
    public void pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        try {
            ((PickDiceResponse) remoteServer.pickDice(userToken, diceId)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickPositionException {
        try {
            ((PickDiceResponse) remoteServer.pickPosition(userToken, position)).handle(this);
        } catch (RemoteException e) {

        }
    }


    @Override
    public void handle(PassTurnResponse response) {

    }
}
