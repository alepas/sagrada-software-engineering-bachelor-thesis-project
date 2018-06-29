package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.notifications.ForceDisconnectionNotification;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.NextAction;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.clientModel.ToolCardInteruptValues;
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
    private String userToken;
    private boolean connected = false;

    public RmiClient() throws NotBoundException, RemoteException{
        Registry registry = LocateRegistry.getRegistry(NetworkConstants.SERVER_ADDRESS, NetworkConstants.RMI_SERVER_PORT);
        remoteServer = (RemoteServer) registry.lookup(NetworkConstants.RMI_CONTROLLER_NAME);
    }

    private void startPolling(){
        new Thread(() -> {
            try {
                while (connected) {
                    remoteServer.poll(userToken);
                    Thread.sleep(NetworkConstants.RMI_POLLING_TIME);
                }
            } catch (RemoteException e){
                connected = false;
                observer.update(null, new ForceDisconnectionNotification(true));
            } catch (InterruptedException e){}
        }).start();
    }

    //-------------------------------- NetworkClientMethods --------------------------------

    @Override
    public void createUser(String username, String password) throws CannotRegisterUserException {
        try {
            CreateUserResponse response = (CreateUserResponse) remoteServer.createUser(username, password, new RmiRemoteObserver());
            response.handle(this);
            if (response.exception == null){
                connected = true;
                userToken = response.userToken;
                startPolling();
            }
        } catch (RemoteException e){

        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        try {
            LoginResponse response = (LoginResponse) remoteServer.login(username, password, new RmiRemoteObserver());
            response.handle(this);
            if (response.exception == null){
                connected = true;
                userToken = response.userToken;
                startPolling();
            }
        } catch (RemoteException e){

        }
    }

    @Override
    public void findGame(String token, int numPlayers, int level) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        try {
            (remoteServer.findGame(token, numPlayers, level)).handle(this);
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
    public void getUserStat(String userToken) throws CannotFindUserInDBException {
        try {
            (remoteServer.getUserStat(userToken)).handle(this);
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
    public NextAction pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.pickDiceForToolCard(userToken, diceId);
            response.handle(this);
            return response.nextAction;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public NextAction placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.placeDiceForToolCard(userToken, diceId, position);
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
    public NextAction interuptToolCard(String userToken, ToolCardInteruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException {
        try {
            ToolCardResponse response = (ToolCardResponse) remoteServer.interuptToolCard(userToken, value);
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
