package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public interface RequestHandler {

    Response handle(CreateUserRequest request);

    Response handle(LoginRequest request);

    Response handle(FindGameRequest request);

    Response handle(PickWpcRequest request);
}
