package it.polimi.ingsw.control.network.commands;

import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.GenericErrorResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;

public interface ResponseHandler {

    void handle(CreateUserResponse response);

    void handle(LoginResponse response);

    void handle(FindGameResponse response);

    void handle(GenericErrorResponse response);


    //Notifications
    void handle(GameStartedNotification response);

    void handle(PlayersChangedNotification response);

    void handle(PrivateObjExtractedNotification response);
}
