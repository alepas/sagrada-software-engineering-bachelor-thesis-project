package it.polimi.ingsw.control.network.rmi;

public interface DisconnectionHandler {
    void notifyDisconnection(String userToken);
}
