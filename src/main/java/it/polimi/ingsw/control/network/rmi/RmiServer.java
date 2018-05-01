package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;

import java.rmi.RemoteException;

public class RmiServer implements RemoteServer {
    private final ServerController controller;

    public RmiServer(ServerController controller) throws RemoteException {
        this.controller = controller;
    };

    @Override
    public Response request(Request request) throws RemoteException {
        return request.handle(controller);
    }
}
