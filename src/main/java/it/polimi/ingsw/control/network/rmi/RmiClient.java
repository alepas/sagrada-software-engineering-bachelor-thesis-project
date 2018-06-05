package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.NextAction;
import it.polimi.ingsw.model.clientModel.Position;
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
            (remoteServer.createUser(username, password)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        try {
            (remoteServer.login(username, password)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void findGame(String token, int numPlayers) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        try {
            RemoteObserver remoteObserver = new RmiRemoteObserver();
            (remoteServer.findGame(token, numPlayers, remoteObserver)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        try {
            (remoteServer.pickWpc(userToken, wpcID)).handle(this);
        } catch (RemoteException e){

        }
    }

    @Override
    public void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        try {
            (remoteServer.passTurn(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public NextAction useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.useToolCard(userToken, cardId);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.pickDiceForToolCard(userToken, diceId, where);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction placeDiceForToolCard(String userToken, int diceId, ClientDiceLocations initialLocation, ClientDiceLocations finalLocation, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.placeDiceForToolCard(userToken, diceId, initialLocation, finalLocation, position);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }


    @Override
    public NextAction pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.pickNumberForToolCard(userToken, number);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }


    @Override
    public void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            (remoteServer.getUpdatedExtractedDices(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            (remoteServer.getUpdatedPOCs(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            (remoteServer.getUpdatedRoundTrack(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            (remoteServer.getUpdatedToolCards(userToken)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        try {
            (remoteServer.getUpdatedWPC(userToken, username)).handle(this);
        } catch (RemoteException e) {

        }
    }

    @Override
    public NextAction getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            UpdatedGameResponse response = (UpdatedGameResponse) remoteServer.getUpdatedGame(userToken);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction stopToolCard(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.stopToolCard(userToken);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.cancelAction(userToken);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }


    @Override
    public NextAction placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        try {
            PlaceDiceResponse response = (PlaceDiceResponse) remoteServer.placeDice(userToken, id, position);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction getNextMove(String userToken) throws PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        try {
            NextMoveResponse response = (NextMoveResponse) remoteServer.getNextMove(userToken);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }
}
