package it.polimi.ingsw.control;

import it.polimi.ingsw.model.usersdb.DatabaseUsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;



public class Controller extends UnicastRemoteObject implements RemoteController {
    //private final HashMap<String, RemoteBaseView> views = new HashMap<>();


    private transient final DatabaseUsers database;

    public Controller() throws RemoteException {
        super();
        database = DatabaseUsers.getInstance();
    }
    @Override
    public synchronized String registerUser(String username, String password) throws RemoteException {
        try {
            return database.registerUser(username, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public synchronized String login(String user, String pwd) throws RemoteException {
        try {
            return database.login(user, pwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public synchronized void addWonGames(String token){
        database.addWonGames(token);
    }
    @Override
    public synchronized int getWonGames(String token){
        return database.getWonGames(token);
    }


}
