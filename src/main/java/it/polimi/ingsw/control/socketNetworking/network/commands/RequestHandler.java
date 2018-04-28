package it.polimi.ingsw.control.socketNetworking.network.commands;

import it.polimi.ingsw.control.socketNetworking.network.commands.requests.CreateUserRequest;

public interface RequestHandler {

    Response handle(CreateUserRequest request);

}
