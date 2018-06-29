package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.cards.PocDB;
import it.polimi.ingsw.server.model.cards.ToolCardDB;
import it.polimi.ingsw.server.model.wpc.WpcDB;
import it.polimi.ingsw.server.network.rmi.RmiServer;
import it.polimi.ingsw.server.network.socket.SocketServer;
import it.polimi.ingsw.shared.constants.NetworkConstants;
import it.polimi.ingsw.shared.network.RemoteServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LaunchServer {

    public static void main(String[] args) throws IOException {
        //Caricamento delle wpc
        WpcDB.getInstance();
        ToolCardDB cardDB=ToolCardDB.getInstance();
        PocDB pocDB= PocDB.getInstance();

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
            RmiServer remoteServer = new RmiServer(ServerController.getInstance());
            RemoteServer stub = (RemoteServer) UnicastRemoteObject.exportObject(remoteServer, NetworkConstants.RMI_SERVER_PORT);

            Registry registry = LocateRegistry.createRegistry(NetworkConstants.RMI_SERVER_PORT);
            registry.rebind(NetworkConstants.RMI_CONTROLLER_NAME, stub);
            System.out.println(">>> RMI Server is running\t\tAddress: " + NetworkConstants.SERVER_ADDRESS + "\tPort: " + NetworkConstants.RMI_SERVER_PORT);
            while (true) Thread.sleep(100*1000);            //Utilizzato per non far terminare questo thread: vedi nota sotto
        } catch (RemoteException e){
            System.out.println(">>> " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("RMI interrotto");
        }


        /* NOTA: Se il thread dovesse terminare non vi sarebbe più alcun oggetto che punti all'RmiServer,
           pertanto il garbage collector provvederà a rimuoverlo e i client non potrebbero più invocarlo */
    }
}