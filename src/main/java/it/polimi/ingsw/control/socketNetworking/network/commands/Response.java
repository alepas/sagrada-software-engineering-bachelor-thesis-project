package it.polimi.ingsw.control.socketNetworking.network.commands;

import java.io.Serializable;

public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}
