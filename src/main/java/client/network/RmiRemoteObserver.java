package client.network;

import shared.network.RemoteObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

public class RmiRemoteObserver extends UnicastRemoteObject implements RemoteObserver {

    private final transient Observer observer = ClientInfo.getInstance();

    public RmiRemoteObserver() throws RemoteException { }

    @Override
    public void update(Observable o, Object arg) throws RemoteException {
        observer.update(null, arg);
    }
}
