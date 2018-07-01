package shared.exceptions.usersAndDatabaseExceptions;

public class CannotCloseOldConnectionException extends Exception{
    private int cause;
    private String user;

    public CannotCloseOldConnectionException(String username, int errorId) {
        cause=errorId;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
            return "C'è stato un problema nella chiusura della connessione precedente per l'utente "+user+".";

        else if (cause==1)
            return "Impossibile trovare una connessione precedente attraverso Socket per l'utente "+user+".";

        return "C'è stato un problema interno nel chiudere la precedente connessione per l'utente "+user+".";
    }


    public int getErrorId() {
        return cause;
    }
}
