package it.polimi.ingsw.control.network;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;

public interface NetworkClient{
    Response request(Request request);
}
