package it.polimi.ingsw.control.network;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.game.Game;

public interface NetworkClient{
    Response request(Request request);

    void startPlaying(ResponseHandler handler, Game game);
}
