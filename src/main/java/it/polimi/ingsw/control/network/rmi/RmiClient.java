package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.responses.GenericErrorResponse;
import it.polimi.ingsw.model.constants.NetworkConstants;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient implements NetworkClient {
    private final RemoteServer remoteServer;

    public RmiClient() throws NotBoundException, RemoteException, MalformedURLException{
        Registry registry = LocateRegistry.getRegistry(NetworkConstants.SERVER_ADDRESS, Registry.REGISTRY_PORT);
        remoteServer = (RemoteServer) registry.lookup(NetworkConstants.RMI_CONTROLLER_NAME);
    }

    @Override
    public Response request(Request request){
        try {
            return remoteServer.request(request);
        } catch (RemoteException e){
            return new GenericErrorResponse(e.getMessage());
        }
    }
}
