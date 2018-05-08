package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.model.game.Game;

public class ClientContext {
    private static ClientContext instance;
    private String username;
    private String userToken;
    private String currentGameID;

    private ClientContext() { }

    public static synchronized ClientContext get() {
        if (instance == null) {
            instance = new ClientContext();
            instance.clean();
        }

        return instance;
    }

    public void clean(){
        this.username = null;
        this.userToken = null;
        this.currentGameID = null;
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

    public String getCurrentGameID() {
        return currentGameID;
    }

    public void setCurrentGameID(String currentGameID) {
        this.currentGameID = currentGameID;
    }

}
