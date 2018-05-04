package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RemoteServer {
    private transient final ServerController controller;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
    };

    @Override
    public Response request(Request request) throws RemoteException {
        return request.handle(controller);
    }

    @Override
    public void addObserver(GameObserver observer, Game game) throws RemoteException {
        if (game != null){
            Game serverGame = DatabaseGames.getInstance().findGameByID(game.getID());
            serverGame.addObserver(observer);
        }
    }
}
