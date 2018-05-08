package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response request(Request request) throws RemoteException;

    void addObserver(RemoteObserver observer, String gameID) throws RemoteException;
}
