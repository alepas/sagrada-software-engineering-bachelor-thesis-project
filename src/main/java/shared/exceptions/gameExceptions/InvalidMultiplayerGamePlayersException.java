package shared.exceptions.gameExceptions;

import server.constants.GameConstants;

public class InvalidMultiplayerGamePlayersException extends Exception {
    private final int numPlayers;

    public InvalidMultiplayerGamePlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    @Override
    public String getMessage() {
        return "Impossibile creare un multiplayer game con questo numero di giocatori: " + numPlayers +
                "\n Una partita multiplayer può essere giocata da " + GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS +
                " a " + GameConstants.MAX_NUM_PLAYERS + " giocatori.";
    }
}