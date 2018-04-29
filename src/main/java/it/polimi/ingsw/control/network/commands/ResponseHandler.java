package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;

public interface ResponseHandler {

    void handle(CreateUserResponse response);

    void handle(LoginResponse response);

}
