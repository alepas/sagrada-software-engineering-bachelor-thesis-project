package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;

public interface RequestHandler {

    Response handle(CreateUserRequest request);

    Response handle(LoginRequest request);

    Response handle(FindGameRequest request);
}
