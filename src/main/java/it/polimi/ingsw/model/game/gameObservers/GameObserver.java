package it.polimi.ingsw.model.game.gameObservers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameObserver extends Remote {

    void onJoin(String username) throws RemoteException;

    void onLeave(String username) throws RemoteException;
}
