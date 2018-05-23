package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}