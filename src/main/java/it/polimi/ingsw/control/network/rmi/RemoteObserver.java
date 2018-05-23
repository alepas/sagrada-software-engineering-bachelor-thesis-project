package it.polimi.ingsw.control.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

public interface RemoteObserver extends Remote {

    public void update(Observable o, Object arg) throws RemoteException;

}
