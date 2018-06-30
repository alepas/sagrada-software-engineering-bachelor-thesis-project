package server.network;

import java.util.Observer;

public abstract class ClientHandler implements Observer {
    protected String userToken;
    protected String username;

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

    public abstract void disconnect();          //Ho perso la connessione con il client
    public abstract void removeConnection();    //Forza il client a disconettersi: serve ad esempio quando un utente già connesso
                                                //logga su una nuova macchina, la vecchia connessione deve essere buttata giù
}
