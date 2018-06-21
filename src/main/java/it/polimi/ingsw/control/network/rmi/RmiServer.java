package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.clientModel.ToolCardInteruptValues;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.rmi.RemoteException;
import java.util.*;

public class RmiServer implements RemoteServer, Observer, DisconnectionHandler {
    private transient final ServerController controller;
    private transient HashMap<String, RemoteObserver> observerByUser;
    private transient HashMap<String, RmiUserConnectionTimer> timerByToken;
    private transient DatabaseUsers userdb = DatabaseUsers.getInstance();

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
        observerByUser = new HashMap<>();
        timerByToken = new HashMap<>();
    };


    //--------------------------------- CONNECTION HANDLER ------------------------------------

    private void reconnectPlayer() {
        //TODO
    }

    private void startUserConnectionDetector(String userToken){
        RmiUserConnectionTimer timer = new RmiUserConnectionTimer(userToken, this);
        timerByToken.put(userToken, timer);
        (new Thread(timer)).start();
    }

    @Override
    public void poll(String userToken) throws RemoteException {
        timerByToken.get(userToken).reset();
    }

    @Override
    public void notifyDisconnection(String userToken) {
        try {
            controller.disconnectUser(userToken);
        } catch (CannotFindPlayerInDatabaseException e) { /*Do nothing: player already disconnected*/}
    }




    //------------------------------------- RMI SERVER ----------------------------------------
    @Override
    public Response createUser(String username, String password) throws CannotRegisterUserException {
        CreateUserResponse response = (CreateUserResponse) controller.createUser(username, password, null);
        startUserConnectionDetector(response.userToken);
        return response;
    }

    @Override
    public Response login(String username, String password) throws CannotLoginUserException {
        LoginResponse response = (LoginResponse) controller.login(username, password, null);
        startUserConnectionDetector(response.userToken);
        return response;
    }

    @Override
    public Response findGame(String userToken, int numPlayers, RemoteObserver observer) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        Response response = controller.findGame(userToken, numPlayers, this);

        String gameID = ((FindGameResponse) response).gameID;
        String username = userdb.getUsernameByToken(userToken);
        RemoteObserver put = observerByUser.put(username, observer);

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
    public Response interuptToolCard(String userToken, ToolCardInteruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException {
        return controller.interuptToolCard(userToken, value);
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
        Response response = controller.findAlreadyStartedGame(userToken, null); //TODO: non ci vuole l'observer?
        String gameID = ((UpdatedGameResponse) response).gameID;

        String username = null;
        try { username = userdb.getUsernameByToken(userToken); }
        catch (CannotFindUserInDBException e) {/*TODO: */}

        observerByUser.put(username, observer);

        return response;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Game){
            Game game = (Game) o;
            for (PlayerInGame player : game.getPlayers()){
                RemoteObserver observer = observerByUser.get(player.getUser());
                if (observer != null){
                    try {
                        observer.update(null, arg);
                    } catch (RemoteException e) {
                        //TODO: non riesce a contattare il client
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
