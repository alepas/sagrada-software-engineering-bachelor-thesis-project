package server.network.rmi;

import server.controller.ServerController;
import shared.clientinfo.Position;

import shared.exceptions.gameexceptions.CannotCreatePlayerException;
import shared.exceptions.gameexceptions.InvalidGameParametersException;
import shared.exceptions.gameexceptions.NotYourWpcException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;

import shared.clientinfo.ToolCardInterruptValues;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.RemoteObserver;
import shared.network.RemoteServer;
import shared.network.commands.responses.CreateUserResponse;
import shared.network.commands.responses.LoginResponse;
import shared.network.commands.responses.Response;


import java.util.HashMap;

public class RmiServer implements RemoteServer {
    private final ServerController controller;
    private HashMap<String, RmiClientHandler> clientByToken;

    /**
     * Sets the controller and creates a new HashMap
     *
     * @param controller is the controller of the server
     */
    public RmiServer(ServerController controller) {
        this.controller = controller;
        clientByToken = new HashMap<>();
    }


    //--------------------------------- CONNECTION HANDLER ------------------------------------

    /**
     * checks if the client associated to the token is different from null and in case calls the client poll
     * @param userToken is the player's token
     */
    @Override
    public void poll(String userToken) {
        RmiClientHandler client = clientByToken.get(userToken);
        if (client != null) client.poll();
    }

    /**
     * deletes the observer from the game and removes the client from all Hashmaps
     *
     * @param handler is the rmi client server
     */
    void removeClient(RmiClientHandler handler){
        try {
            controller.deleteObserverFromGame(handler.getUsername(),handler);
        } catch (CannotFindPlayerInDatabaseException e) { /*Do nothing: user not in game*/ }
        controller.removeSessionFromDatabase(handler.getToken());
        clientByToken.remove(handler.getToken());
        controller.removeClient(handler.getUsername(),handler);
    }


    //------------------------------------- RMI SERVER ----------------------------------------

    /**
     * calls the create user response, adds the client connection to the controller and all client parameters
     * @param username is the player's username
     * @param password is the player's password
     * @param observer is the observer related to the player
     *
     * @return  the response of the create user
     * @throws CannotRegisterUserException if it not possible to register the user
     */
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


    /**
     * @param username is the player's username
     * @param password is the accont password
     * @param observer
     * @return
     * @throws CannotLoginUserException
     */
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
