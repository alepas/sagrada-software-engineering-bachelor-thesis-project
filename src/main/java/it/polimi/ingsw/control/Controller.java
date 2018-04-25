package it.polimi.ingsw.control;

import it.polimi.ingsw.model.usersdb.DatabaseUsers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Controller extends UnicastRemoteObject implements RemoteController {
    //private final HashMap<String, RemoteBaseView> views = new HashMap<>();


    private transient final DatabaseUsers database;

    public Controller() throws RemoteException {
        super();
        database = DatabaseUsers.getInstance();
    }
    @Override
    public synchronized String registerUser(String username, String password) throws RemoteException {

            return database.registerUser(username, password);

    }
    @Override
    public synchronized String login(String user, String pwd) throws RemoteException {

            return database.login(user, pwd);


    }
    @Override
    public synchronized void addWonGames(String token){
        database.addWonGamesFromToken(token);
    }
    @Override
    public synchronized int getWonGames(String token){
        return database.getWonGamesFromToken(token);
    }


}
