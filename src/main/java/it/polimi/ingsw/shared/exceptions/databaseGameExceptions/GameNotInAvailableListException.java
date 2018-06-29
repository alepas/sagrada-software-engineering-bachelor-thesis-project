package it.polimi.ingsw.shared.exceptions.databaseGameExceptions;

import it.polimi.ingsw.server.model.game.Game;

public class GameNotInAvailableListException  extends Exception {
    private final Game game;

    public GameNotInAvailableListException(Game game){
        this.game = game;
    }

    @Override
    public String getMessage() {
        return "Game non found in available list: " + game.getID();
    }
}
