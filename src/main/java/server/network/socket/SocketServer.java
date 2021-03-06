package server.network.socket;

import shared.constants.NetworkConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private boolean run = true;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        System.out.println(">>> Socket server's listening\tAddress: " + NetworkConstants.SERVER_ADDRESS + "\tPort: " + port);
    }

    public void run() throws IOException {
        while (run) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(">>> New connection " + clientSocket.getRemoteSocketAddress());
            pool.submit(new SocketClientHandler(clientSocket));
        }
    }

    public void close() throws IOException {
        run = false;
        serverSocket.close();
        pool.shutdown();
    }
}
