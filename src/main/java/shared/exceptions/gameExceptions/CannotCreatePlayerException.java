package shared.exceptions.gameExceptions;

public class CannotCreatePlayerException extends Exception {
    private final String username;

    public CannotCreatePlayerException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "Impossibile creare l'oggetto PlayerInGame per l'utente: " + username;
    }
}
