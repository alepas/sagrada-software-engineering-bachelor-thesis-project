package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.game.Game;

public class FindGameResponse implements Response {
    public final Game game;
    public final String error;

    public FindGameResponse(Game game, String error) {
        this.game = game;
        this.error = error;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}