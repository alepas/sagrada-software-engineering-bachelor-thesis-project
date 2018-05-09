package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.responses.notifications.Notification;
import org.mockito.internal.matchers.Not;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

public class RmiRemoteObserver extends UnicastRemoteObject implements RemoteObserver {

    private transient NotificationHandler handler;

    public RmiRemoteObserver(NotificationHandler handler) throws RemoteException {
        this.handler = handler;
    }

    @Override
    public void update(Observable o, Object arg) throws RemoteException {
        ((Notification) arg).handle(handler);
    }
}
