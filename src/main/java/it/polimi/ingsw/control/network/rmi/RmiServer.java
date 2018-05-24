package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
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
    public Response passTurn(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        return controller.passTurn(userToken);
    }

    @Override
    public Response pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        return controller.pickDice(userToken, diceId);
    }

    @Override
    public Response pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, CannotPickPositionException {
        return controller.pickPosition(userToken, position);
    }

    @Override
    public Response useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException {
        return controller.setToolCard(userToken, cardId);
    }

    @Override
    public Response pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException {
        return controller.pickDiceForToolCard(userToken, diceId, where);
    }

    @Override
    public Response pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException {
        return controller.pickPositionForToolCard(userToken, position);
    }

    @Override
    public Response pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException {
        return controller.pickColorForToolCard(userToken, color);
    }

    @Override
    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException {
        return controller.pickNumberForToolCard(userToken,number);
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
