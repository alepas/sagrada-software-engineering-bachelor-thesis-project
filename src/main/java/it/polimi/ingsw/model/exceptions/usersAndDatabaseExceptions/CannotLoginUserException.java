package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class CannotLoginUserException extends Exception{
    private int cause;
    private String user;

    public CannotLoginUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
            return "Impossibile eseguire l'accesso per l'utente " + user + " a causa di un problema interno";

        else if (cause==1)
            return "La password per l'utente " + user + " è sbagliata. \r\nProva nuovamente.";
        else if (cause==2)
            return "L'utente " + user + " non esiste.";
        if (cause==3)
            return "Impossibile eseguire l'accesso per l'utente " + user+ ".\r\nC'è stato un problema nel chiudere la connessione precedente.";

        else return "C'è stato un problema interno nel processo di login.";
    }

    public int getErrorId() {
        return cause;
    }
}
