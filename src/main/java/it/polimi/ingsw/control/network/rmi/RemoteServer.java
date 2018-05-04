package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response request(Request request) throws RemoteException;

    void addObserver(GameObserver observer, Game game) throws RemoteException;
}
