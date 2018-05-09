package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.model.dicebag.Color;

import java.util.Observable;
import java.util.Observer;

public class ClientModel implements Observer {
    private static ClientModel instance;
    private Observer observer;

    //User
    private String username;
    private String userToken;

    //Game
    private String gameID;
    private int gameActualPlayers;
    private int gameNumPlayers;
    private Color[] privateObjectives;

    private ClientModel() { }

    public static synchronized ClientModel getInstance() {
        if (instance == null) {
            instance = new ClientModel();
            instance.clean();
        }

        return instance;
    }

    public void clean(){
        this.username = null;
        this.userToken = null;
        this.gameID = null;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getGameActualPlayers() {
        return gameActualPlayers;
    }

    public void setGameActualPlayers(int gameActualPlayers) {
        this.gameActualPlayers = gameActualPlayers;
    }

    public int getGameNumPlayers() {
        return gameNumPlayers;
    }

    public void setGameNumPlayers(int gameNumPlayers) {
        this.gameNumPlayers = gameNumPlayers;
    }

    public Color[] getPrivateObjectives() {
        return privateObjectives;
    }

    public void setPrivateObjectives(Color[] privateObjectives) {
        this.privateObjectives = privateObjectives;
    }

    @Override
    public void update(Observable o, Object arg) {
        observer.update(o, arg);
    }
}
