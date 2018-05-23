package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
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
