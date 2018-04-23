package it.polimi.ingsw.control;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteController extends Remote {

    String registerUser(String username, String password) throws RemoteException;

    String login(String user, String pwd) throws RemoteException ;

    void addWonGames(String token) throws RemoteException;

    int getWonGames(String token) throws RemoteException;

}
