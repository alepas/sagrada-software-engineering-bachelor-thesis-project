package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class RmiServer extends UnicastRemoteObject implements RemoteServer, Observer {
    private transient final ServerController controller;
    private transient HashMap<String, ArrayList<RemoteObserver>> observersByGame;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
        observersByGame = new HashMap<>();
    };

    private void reconnectPlayer() {
        //TODO
    }

    @Override
    public Response createUser(String username, String password) throws CannotRegisterUserException {
        return controller.createUser(username, password, null);
    }

    @Override
    public Response login(String username, String password) throws CannotLoginUserException {
        return controller.login(username, password, null);
    }

    @Override
    public Response findGame(String userToken, int numPlayers, RemoteObserver observer) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        Response response = controller.findGame(userToken, numPlayers, this);

        String gameID = ((FindGameResponse) response).gameID;
        if (observersByGame.get(gameID) == null) observersByGame.put(gameID, new ArrayList<>());
        if (!observersByGame.get(gameID).contains(observer)) observersByGame.get(gameID).add(observer);

        return response;
    }

    @Override
    public Response pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        return controller.pickWpc(userToken, wpcID);
    }

    @Override
    public Response passTurn(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException , CannotPerformThisMoveException {
        return controller.passTurn(userToken);
    }

    @Override
    public Response pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        return controller.pickDice(userToken, diceId);
    }

    @Override
    public Response pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, CannotPickPositionException, CannotPerformThisMoveException {
        return controller.pickPosition(userToken, position);
    }

    @Override
    public Response useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        return controller.setToolCard(userToken, cardId);
    }

    @Override
    public Response pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.pickDiceForToolCard(userToken, diceId, where);
    }

    @Override
    public Response pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.pickPositionForToolCard(userToken, position);
    }

    @Override
    public Response pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.pickColorForToolCard(userToken, color);
    }

    @Override
    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        return controller.pickNumberForToolCard(userToken,number);
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
        return controller.getUpdatedGame(userToken);
    }

    @Override
    public Response stopToolCard(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException {
        return controller.stopToolCard(userToken);
    }

    @Override
    public Response cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        return controller.cancelAction(userToken);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Game){
            Game game = (Game) o;
            ArrayList<RemoteObserver> observers = observersByGame.get(game.getID());
            for (RemoteObserver observer : observers){
                try {
                    observer.update(null, arg);
                } catch (RemoteException e){
                    //TODO
                    e.printStackTrace();
                }
            }
        }
    }
}
