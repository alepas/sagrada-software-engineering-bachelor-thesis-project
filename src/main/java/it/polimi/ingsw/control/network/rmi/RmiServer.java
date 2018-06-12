package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.control.network.commands.responses.UpdatedGameResponse;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class RmiServer implements RemoteServer, Observer {
    private transient final ServerController controller;
    private transient HashMap<String, ArrayList<RemoteObserver>> observersByGame;
    private transient HashMap<String, RemoteObserver> observersByUser;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
        observersByGame = new HashMap<>();
        observersByUser=new HashMap<>();

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
        DatabaseUsers userdb=DatabaseUsers.getInstance();
        String username=null;
        RemoteObserver tempObs=null;
        try {
            username=userdb.getUsernameByToken(userToken);
        } catch (CannotFindUserInDBException e) {

        }
        String gameID = ((FindGameResponse) response).gameID;
        if (observersByGame.get(gameID) == null) observersByGame.put(gameID, new ArrayList<>());
        if (!observersByGame.get(gameID).contains(observer)) observersByGame.get(gameID).add(observer);
        observersByUser.put(username,observer);

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
    public Response useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        return controller.setToolCard(userToken, cardId);
    }

    @Override
    public Response pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.pickDiceForToolCard(userToken, diceId);
    }

    @Override
    public Response placeDiceForToolCard(String userToken, int diceId, Position position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        return controller.placeDiceForToolCard(userToken, diceId, position);
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
    public Response placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        return controller.placeDice(userToken, id, position);
    }

    @Override
    public Response getNextMove(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        return controller.getNextMove(userToken);
    }

    @Override
    public Response getUserStat(String userToken) throws RemoteException, CannotFindUserInDBException {
        return controller.getUserStat(userToken);
    }

    @Override
    public Response findAlreadyStartedGame(String userToken, RemoteObserver observer) throws RemoteException, CannotFindGameForUserInDatabaseException {
        Response response= controller.findAlreadyStartedGame(userToken, null);
        DatabaseUsers userdb=DatabaseUsers.getInstance();
        String username=null;
        RemoteObserver tempObs=null;
        try {
            username=userdb.getUsernameByToken(userToken);
        } catch (CannotFindUserInDBException e) {

        }
        String gameID = ((UpdatedGameResponse) response).gameID;
        if (observersByGame.get(gameID) == null) observersByGame.put(gameID, new ArrayList<>());
        if ((tempObs=observersByUser.get(username))!=null) observersByGame.get(gameID).remove(tempObs);
        if (!observersByGame.get(gameID).contains(observer)) observersByGame.get(gameID).add(observer);
        observersByUser.put(username,observer);

        return response;
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
