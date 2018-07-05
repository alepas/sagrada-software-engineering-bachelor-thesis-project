package server.network;

import java.util.Observer;


public abstract class ClientHandler implements Observer {
    protected String userToken;
    protected String username;

    /**
     * @param userToken is the player's token
     */
    public ClientHandler(String userToken) {
        this.userToken = userToken;
        this.username=null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return userToken;
    }

    /**
     * the connection has been lost
     */
    public abstract void disconnect();


    /**
     * force the client to disconnect, it is necessary when a player log on an other machine.
     * the old one must be canceled
     */
    public abstract void removeConnection();
}
