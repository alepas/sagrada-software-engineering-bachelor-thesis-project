package server.network.rmi;

import server.network.ClientHandler;
import shared.network.RemoteObserver;
import shared.network.commands.notifications.ForceDisconnectionNotification;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public class RmiClientHandler extends ClientHandler implements Observer {
    private RemoteObserver observer;
    private final RmiUserConnectionTimer timer;
    private final RmiServer rmiServer;

    /**
     * @param userToken is the player's token
     * @param rmiServer is the connection to the rmi server
     */
    RmiClientHandler(String userToken, RmiServer rmiServer) {
        super(userToken);
        observer = null;
        this.rmiServer = rmiServer;

        timer = new RmiUserConnectionTimer(this);
        (new Thread(timer)).start();
    }

    public void setObserver(RemoteObserver observer){
        this.observer = observer;
    }

    public void removeObserver(){
        this.observer = null;
    }

    /**
     * if the connection to the client has been lost it removes the client from the rmiServer
     */
    @Override
    public void disconnect() {
        System.out.println("Rmi client disconnesso");
        rmiServer.removeClient(this);
        observer = null;
    }

    /**
     * force the player to disconnect
     */
    @Override
    public void removeConnection() {
        System.out.println("Rmi client buttato fuori");
        timer.stop();
        rmiServer.removeClient(this);
        update(null, new ForceDisconnectionNotification(false));
        observer = null;
    }

    /**
     * reset the polling timer
     */
    void poll(){
        timer.reset();
    }

    /**
     * @param o is an observer
     * @param arg is the update argument
     */
    @Override
    public void update(Observable o, Object arg) {
        try {
            observer.update(null, arg);
        } catch (RemoteException e) {
            disconnect();
        }
    }
}
