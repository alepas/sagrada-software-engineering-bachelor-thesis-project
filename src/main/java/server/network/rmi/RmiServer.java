package server.network.rmi;

import server.controller.ServerController;
import shared.clientInfo.Position;

import shared.exceptions.gameexceptions.CannotCreatePlayerException;
import shared.exceptions.gameexceptions.InvalidGameParametersException;
import shared.exceptions.gameexceptions.NotYourWpcException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;

import shared.clientInfo.ToolCardInterruptValues;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.RemoteObserver;
import shared.network.RemoteServer;
import shared.network.commands.responses.CreateUserResponse;
import shared.network.commands.responses.LoginResponse;
import shared.network.commands.responses.Response;

import java.rmi.RemoteException;
import java.util.HashMap;

public class RmiServer implements RemoteServer {
    private transient final ServerController controller;
    private transient HashMap<String, RmiClientHandler> clientByToken;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
        clientByToken = new HashMap<>();
    }


    //--------------------------------- CONNECTION HANDLER ------------------------------------

    @Override
    public void poll(String userToken) throws RemoteException {
        RmiClientHandler client = clientByToken.get(userToken);
        if (client != null) client.poll();
    }


    public void removeRemoteObserver(String username) {
//        observerByUser.remove(username);
    }

    //TODO: Non serve più
    public void rmiDisconnectionTimerStop(String oldToken) {
//        RmiUserConnectionTimer timer = timerByToken.remove(oldToken);
//        if (timer != null){
//            timer.stop();
//        }
    }

    void removeClient(RmiClientHandler handler){
        try {
            controller.deleteObserverFromGame(handler.getUsername(),handler);
        } catch (CannotFindPlayerInDatabaseException e) { /*Do nothing: user not in game*/ }
        controller.removeSessionFromDatabase(handler.getToken());
        clientByToken.remove(handler.getToken());
        controller.removeClient(handler.getUsername(),handler);
    }


    //------------------------------------- RMI SERVER ----------------------------------------

    @Override
    public Response createUser(String username, String password, RemoteObserver observer) throws CannotRegisterUserException {
        CreateUserResponse response = (CreateUserResponse) controller.createUser(username, password, null);
        RmiClientHandler client = new RmiClientHandler(response.userToken, this);
        controller.addClientConnection(response.username, client);
        client.setObserver(observer);
        client.setUsername(response.username);
        clientByToken.put(response.userToken, client);
        return response;
    }

    @Override
    public Response login(String username, String password, RemoteObserver observer) throws CannotLoginUserException {
        LoginResponse response = (LoginResponse) controller.login(username, password, null);
        RmiClientHandler client = new RmiClientHandler(response.userToken, this);
        controller.addClientConnection(response.username, client);
        client.setObserver(observer);
        client.setUsername(response.username);
        clientByToken.put(response.userToken, client);
        return response;
    }

    @Override
    public Response findGame(String userToken, int numPlayers, int level) throws CannotFindUserInDBException, InvalidGameParametersException, CannotCreatePlayerException {
        return controller.findGame(userToken, numPlayers, level, clientByToken.get(userToken));
    }

    @Override
    public Response pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        return controller.pickWpc(userToken, wpcID);
    }

    @Override
    public Response passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        return controller.passTurn(userToken);
    }

    @Override
    public Response useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        return controller.setToolCard(userToken, cardId);
    }

    @Override
    public Response pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.pickDiceForToolCard(userToken, diceId);
    }

    @Override
    public Response placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.placeDiceForToolCard(userToken, diceId, position);
    }

    @Override
    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        return controller.pickNumberForToolCard(userToken, number);
    }

    @Override
    public Response getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getUpdatedExtractedDices(userToken);
    }

    @Override
    public Response getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getUpdatedPOCs(userToken);
    }

    @Override
    public Response getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getUpdatedRoundTrack(userToken);
    }

    @Override
    public Response getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getUpdatedToolCards(userToken);
    }

    @Override
    public Response getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        return controller.getUpdatedWPC(userToken, username);
    }

    @Override
    public Response getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getUpdatedGame(userToken, clientByToken.get(userToken));
    }

    @Override
    public Response interuptToolCard(String userToken, ToolCardInterruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInterruptToolCardException, NoToolCardInUseException {
        return controller.interruptToolCard(userToken, value);
    }

    @Override
    public Response cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        return controller.cancelAction(userToken);
    }

    @Override
    public Response placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        return controller.placeDice(userToken, id, position);
    }

    @Override
    public Response getNextMove(String userToken) throws CannotFindPlayerInDatabaseException {
        return controller.getNextMove(userToken);
    }

    @Override
    public Response getUserStat(String userToken) throws CannotFindUserInDBException {
        return controller.getUserStat(userToken);
    }

}
