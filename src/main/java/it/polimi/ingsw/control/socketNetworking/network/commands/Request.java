package it.polimi.ingsw.control.socketNetworking.network.commands;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}
