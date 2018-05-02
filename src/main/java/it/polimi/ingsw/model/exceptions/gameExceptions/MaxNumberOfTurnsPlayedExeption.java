package it.polimi.ingsw.model.exceptions.gameExceptions;

import it.polimi.ingsw.model.game.Game;

public class MaxNumberOfTurnsPlayedExeption extends RuntimeException {
    private final Game game;

    public MaxNumberOfTurnsPlayedExeption(Game game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Cannot play another turn in this game: " + game.getGameID();
    }
}
