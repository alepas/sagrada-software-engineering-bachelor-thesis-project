package it.polimi.ingsw.model.exceptions.gameExceptions;

public class InvalidPlayersException extends RuntimeException {
    private final int numPlayers;

    public InvalidPlayersException(int numPlayers){
        this.numPlayers = numPlayers;
    }

    @Override
    public String getMessage() {
        return "Cannot find a game with this amount of players: " + numPlayers;
    }
}