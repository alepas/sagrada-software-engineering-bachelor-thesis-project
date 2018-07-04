package server;

import server.controller.ServerController;
import server.model.configLoader.ConfigLoader;
import server.network.rmi.RmiServer;
import server.network.socket.SocketServer;
import shared.constants.NetworkConstants;
import shared.network.RemoteServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LaunchServer {

    public static void main(String[] args) throws IOException {
        ConfigLoader.loadConfig();

        //Avvio di RMI
        try {
            RmiServer remoteServer = new RmiServer(ServerController.getInstance());
            RemoteServer stub = (RemoteServer) UnicastRemoteObject.exportObject(remoteServer, NetworkConstants.RMI_SERVER_PORT);

            Registry registry = LocateRegistry.createRegistry(NetworkConstants.RMI_SERVER_PORT);
            registry.rebind(NetworkConstants.RMI_CONTROLLER_NAME, stub);
            System.out.println(">>> RMI Server is running\t\tAddress: " + NetworkConstants.SERVER_ADDRESS + "\tPort: " + NetworkConstants.RMI_SERVER_PORT);
        } catch (RemoteException e) {
            System.out.println(">>> " + e.getMessage());
        }

        //Avvio della socket

        boolean restart;
        do {
            restart = false;
            try {
                SocketServer server = new SocketServer(NetworkConstants.SOCKET_SERVER_PORT);

                try {
                    server.run();
                } finally {
                    server.close();
                }
            } catch (IOException e) {
                restart = true;
            }
        } while (restart);



        /* NOTA: Se il thread dovesse terminare non vi sarebbe più alcun oggetto che punti all'RmiServer,
           pertanto il garbage collector provvederà a rimuoverlo e i client non potrebbero più invocarlo */
    }
}
