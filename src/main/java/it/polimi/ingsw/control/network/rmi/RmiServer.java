package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;

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
        return controller.createUser(username, password);
    }

    @Override
    public Response login(String username, String password) throws CannotLoginUserException {
        return controller.login(username, password);
    }

    @Override
    public Response findGame(String userToken, int numPlayers) throws CannotFindUserInDBException, InvalidPlayersException, NullTokenException {
        return controller.findGame(userToken, numPlayers);
    }

    @Override
    public void addObserver(RemoteObserver observer, String gameID) throws RemoteException {
        Game serverGame = DatabaseGames.getInstance().findGameByID(gameID);

        if (serverGame != null){
            if (observersByGame.get(gameID) == null) observersByGame.put(gameID, new ArrayList<>());
            if (observersByGame.get(gameID).contains(observer)) return;
            observersByGame.get(gameID).add(observer);
            serverGame.addObserver(this);
        } else {
            //TODO: segnalare errore
        }
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
