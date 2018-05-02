package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.rmi.RemoteServer;
import it.polimi.ingsw.control.network.rmi.RmiServer;
import it.polimi.ingsw.control.network.socket.SocketServer;
import it.polimi.ingsw.model.constants.NetworkConstants;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LaunchServer {

    public static void main(String[] args) throws IOException {
        //Avvio della socket
        new Thread(){
            @Override
            public void run() {
                try {
                    SocketServer server = new SocketServer(NetworkConstants.SOCKET_SERVER_PORT);

                    try {
                        server.run();
                    } finally {
                        server.close();
                    }
                } catch (IOException e){
                    this.interrupt();
                }
            }
        }.start();

        //Avvio di RMI
        try {
            Registry registry = LocateRegistry.getRegistry();
            RmiServer rmiServer = new RmiServer(ServerController.getInstance());
            RemoteServer remoteServer = (RemoteServer) UnicastRemoteObject.exportObject(rmiServer, 0);

            registry.rebind(NetworkConstants.RMI_CONTROLLER_NAME, remoteServer);
            System.out.println(">>> RMI Server is running");
        } catch (RemoteException e){
            System.out.println(">>> " + e.getMessage());
        }
    }
}
