package it.polimi.ingsw.control.network;

public abstract class ClientHandler {
    protected String userToken;

    public ClientHandler(String userToken) {
        this.userToken = userToken;
    }

    public String getToken() {
        return userToken;
    }

    public abstract void disconnect();          //Ho perso la connessione con il client
    public abstract void removeConnection();    //Forza il client a disconettersi: serve ad esempio quando un utente già connesso
                                                //logga su una nuova macchina, la vecchia connessione deve essere buttata giù
}
