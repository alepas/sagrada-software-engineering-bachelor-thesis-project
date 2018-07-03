package shared.exceptions.gameExceptions;

public class InvalidMultiplayerGamePlayersException extends Exception {
    private final int numPlayers;
    private final int min_players;
    private final int max_players;

    public InvalidMultiplayerGamePlayersException(int numPlayers, int min_players, int max_players) {
        this.numPlayers = numPlayers;
        this.min_players = min_players;
        this.max_players = max_players;
    }

    @Override
    public String getMessage() {
        return "Impossibile creare un multiplayer game con questo numero di giocatori: " + numPlayers +
                "\n Una partita multiplayer può essere giocata da " + min_players +
                " a " + max_players + " giocatori.";
    }
}