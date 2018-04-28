package it.polimi.ingsw.control.socketNetworking.network.commands;

import it.polimi.ingsw.control.socketNetworking.network.commands.responses.CreateUserResponse;

public interface ResponseHandler {

    void handle(CreateUserResponse response);

}
