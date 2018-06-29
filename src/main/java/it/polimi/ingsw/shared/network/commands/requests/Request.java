package it.polimi.ingsw.shared.network.commands.requests;

import it.polimi.ingsw.shared.network.commands.responses.Response;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}