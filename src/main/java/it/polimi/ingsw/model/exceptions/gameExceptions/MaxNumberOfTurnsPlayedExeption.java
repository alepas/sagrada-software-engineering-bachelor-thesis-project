package it.polimi.ingsw.model.exceptions.gameExceptions;

import it.polimi.ingsw.model.game.AbstractGame;

public class MaxNumberOfTurnsPlayedExeption extends RuntimeException {
    private final AbstractGame game;

    public MaxNumberOfTurnsPlayedExeption(AbstractGame game) {
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Cannot play another turn in this game: " + game.getGameID();
    }
}
