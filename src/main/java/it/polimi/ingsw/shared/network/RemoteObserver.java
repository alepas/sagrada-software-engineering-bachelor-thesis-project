package it.polimi.ingsw.shared.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

public interface RemoteObserver extends Remote {

    @SuppressWarnings( "deprecation" )
    void update(Observable o, Object arg) throws RemoteException;

}
