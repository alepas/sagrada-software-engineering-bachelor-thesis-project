package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

public class RmiRemoteObserver extends UnicastRemoteObject implements RemoteObserver {

    private transient ResponseHandler handler;

    public RmiRemoteObserver(ResponseHandler handler) throws RemoteException {
        this.handler = handler;
    }

    @Override
    public void update(Observable o, Object arg) throws RemoteException {
        ((Response) arg).handle(handler);
    }
}
