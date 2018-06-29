package shared.exceptions.usersAndDatabaseExceptions;

public class CannotRegisterUserException extends Exception{
    private int cause;
    private String user;

    public CannotRegisterUserException(String username, int cause) {
        this.cause=cause;
        user = username;
    }
    @Override
    public String getMessage() {
        if (cause==0)
        return "Impossibile creare un nuovo utente \"" + user +"\" a causa di un problema interno.";
        else if (cause==1)
            return "L'username \""+user+"\" è stato già scelto.\r\nImpossibile registrare un nuovo utente con lo stesso username.";
        else return "C'è stato un errore nel processo di registrazione.\r\nL'utente "+ user +" non può essere creato.";
    }
    public int getErrorId() {
        return cause;
    }

}
