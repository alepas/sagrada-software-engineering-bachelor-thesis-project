package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.constants.NetworkConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
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
    public void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        try {
            remoteServer.passTurn(userToken).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        try {
            ((MoveResponse) remoteServer.useToolCard(userToken, cardId)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        try {
            ((MoveResponse) remoteServer.pickDiceForToolCard(userToken, diceId, where)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void placeDiceForToolCard(String userToken, int diceId, ClientDiceLocations diceFrom, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        try {
            ((MoveResponse) remoteServer.placeDiceForToolCard(userToken, diceId,diceFrom,position)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException, CannotPerformThisMoveException {
        try {
            ((MoveResponse) remoteServer.pickColorForToolCard(userToken, color)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        try {
            ((MoveResponse) remoteServer.pickNumberForToolCard(userToken, number)).handle(this);
        } catch (RemoteException e){

        }
    }


    @Override
    public void pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        try {
            ((PickDiceResponse) remoteServer.pickDice(userToken, diceId)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickPositionException, CannotPerformThisMoveException {
        try {
            ((PickPositionResponse) remoteServer.pickPosition(userToken, position)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            ((UpdatedExtractedDicesResponse) remoteServer.getUpdatedExtractedDices(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            ((UpdatedPOCsResponse) remoteServer.getUpdatedPOCs(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            ((UpdatedRoundTrackResponse) remoteServer.getUpdatedRoundTrack(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            ((UpdatedToolCardsResponse) remoteServer.getUpdatedToolCards(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        try {
            ((UpdatedWPCResponse) remoteServer.getUpdatedWPC(userToken, username)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            ((UpdatedGameResponse) remoteServer.getUpdatedGame(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void stopToolCard(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException {
        try {
            ((MoveResponse) remoteServer.stopToolCard(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        try {
            ((MoveResponse) remoteServer.cancelAction(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }


}
