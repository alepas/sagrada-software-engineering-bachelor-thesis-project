package it.polimi.ingsw.model;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.rmi.RemoteServer;
import it.polimi.ingsw.control.network.rmi.RmiServer;
import it.polimi.ingsw.control.network.socket.SocketServer;
import it.polimi.ingsw.model.constants.NetworkConstants;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LaunchServer {


    // NOTA: Prima di far partire il server eseguire il comando './launch_registry.sh' dal terminale di intellij
    // e aspettare un minuto circa (tempo necessario affinchè il registry sia caricato)

    public static void main(String[] args) throws IOException {
        //Caricamento delle wpc
        WpcDB.getInstance();

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
            RemoteServer remoteServer = new RmiServer(new ServerController(null));

            registry.rebind(NetworkConstants.RMI_CONTROLLER_NAME, remoteServer);
            System.out.println(">>> RMI Server is running");
        } catch (RemoteException e){
            System.out.println(">>> " + e.getMessage());
        }
    }
}
