package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.ClientHandler;
import it.polimi.ingsw.control.network.commands.notifications.ForceDisconnectionNotification;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public class RmiClientHandler extends ClientHandler implements Observer {
    private RemoteObserver observer;
    private final RmiUserConnectionTimer timer;
    private final RmiServer rmiServer;

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

    @Override
    public void disconnect() {
        //Ho perso la connessione con il client
        System.out.println("Rmi client disconnesso");
        rmiServer.removeClient(userToken, this);
        observer = null;
    }

    @Override
    public void removeConnection() {
        //Forza il client a disconettersi
        System.out.println("Rmi client buttato fuori");
        timer.stop();
        update(null, new ForceDisconnectionNotification(false));
        rmiServer.removeClient(userToken, this);
        observer = null;
    }

    void poll(){
        timer.reset();
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            observer.update(null, arg);
        } catch (RemoteException e) {
            //TODO: non riesce a contattare il client
            e.printStackTrace();
        }
    }
}
