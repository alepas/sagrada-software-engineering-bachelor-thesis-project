package it.polimi.ingsw.shared.exceptions.gameExceptions;

public class InvalidSinglePlayerGamePlayersException extends Exception {
    private final int numPlayers;

    public InvalidSinglePlayerGamePlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    @Override
    public String getMessage() {
        return "Impossibile creare un single player game con questo numero di giocatori: " + numPlayers +
                "\n Una partita singleplayer può essere giocata da 1 giocatore";
    }
}