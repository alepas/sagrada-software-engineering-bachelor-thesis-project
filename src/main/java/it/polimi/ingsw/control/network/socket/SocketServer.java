package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.socket.SocketClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        System.out.println(">>> Listening on " + port);
    }

    public void run() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(">>> New connection " + clientSocket.getRemoteSocketAddress());
            pool.submit(new SocketClientHandler(clientSocket));
            if(false) break;        //Non cambia nulla ma in questo modo IntelliJ non segnala errore
                                    //perchè "c'è un modo per uscire dal loop"
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        pool.shutdown();
    }
}
