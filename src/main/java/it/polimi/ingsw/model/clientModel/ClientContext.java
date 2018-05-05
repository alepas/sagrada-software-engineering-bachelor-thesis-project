package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.model.game.Game;

public class ClientContext {
    private static ClientContext instance;
    private String username;
    private String userToken;
    private Game currentGame;

    private boolean gameStarted;

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
        this.currentGame = null;
        this.gameStarted = false;
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

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

}
