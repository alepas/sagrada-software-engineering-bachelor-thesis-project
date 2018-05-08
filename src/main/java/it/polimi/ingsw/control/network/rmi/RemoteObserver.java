package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

public interface RemoteObserver extends Remote {

    public void update(Observable o, Object arg) throws RemoteException;

}
