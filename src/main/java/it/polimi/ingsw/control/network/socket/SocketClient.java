package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.requests.PickWpcRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.PickWpcResponse;
import it.polimi.ingsw.control.network.commands.responses.notifications.*;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observer;

public class SocketClient extends NetworkClient implements ResponseHandler {
    private final String host;
    private final int port;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientModel clientModel = ClientModel.getInstance();
    private Observer observer = ClientModel.getInstance();

    private Thread receiver;

    private Response lastResponse;
    private final Object responseWaiter = new Object();

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        connection = new Socket(host, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
        lastResponse = null;
        startReceiving();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    public void request(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            throw new RuntimeException("Exception on network: " + e.getMessage());
        }
    }
    
    public void startReceiving() {
        receiver = new Thread(
                () -> {
                    Response response = null;
                    do {
                        try {
                            response = (Response) in.readObject();
                            if (response != null) response.handle(this);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } while (true);
                }
        );
        receiver.start();
    }


    //-------------------------------- NetworkClientMethods --------------------------------

    @Override
    public void createUser(String username, String password) throws CannotRegisterUserException {
        request(new CreateUserRequest(username, password));
        synchronized (responseWaiter){
            try {
                if (lastResponse == null) responseWaiter.wait();
                CreateUserResponse createUserResponse = (CreateUserResponse) lastResponse;
                Exception e = createUserResponse.exception;
                lastResponse = null;
                if (e != null) throw (CannotRegisterUserException) e;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        request(new LoginRequest(username, password));
    }

    @Override
    public void findGame(String token, int numPlayers) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        request(new FindGameRequest(token, numPlayers));
    }

    @Override
    public void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        request(new PickWpcRequest(wpcID, userToken));
    }


    //-------------------------------- Response Handler --------------------------------

    @Override
    public void handle(CreateUserResponse response) throws CannotRegisterUserException {
        synchronized (responseWaiter) {
            lastResponse = response;
            if (response.exception == null) {
                clientModel.setUsername(response.username);
                clientModel.setUserToken(response.userToken);
            }
            notifyAll();
        }
    }

    @Override
    public void handle(LoginResponse response) throws CannotLoginUserException{
        if(response.userToken == null){
            clientModel.clean();
            if (response.exception instanceof CannotLoginUserException) throw (CannotLoginUserException) response.exception;
            return;
        }
        clientModel.setUsername(response.username);
        clientModel.setUserToken(response.userToken);
    }

    @Override
    public void handle(FindGameResponse response) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException {
        String gameID = response.gameID;
        if (gameID != null) {
            clientModel.setGameID(gameID);
            clientModel.setGameActualPlayers(response.actualPlayers);
            clientModel.setGameNumPlayers(response.numPlayers);
            startReceiving();
        } else {
            if (response.exception instanceof InvalidNumOfPlayersException) throw (InvalidNumOfPlayersException) response.exception;
            if (response.exception instanceof CannotFindUserInDBException) throw (CannotFindUserInDBException) response.exception;
            if (response.exception instanceof CannotCreatePlayerException) throw (CannotCreatePlayerException) response.exception;
        }
    }

    @Override
    public void handle(PickWpcResponse response) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        Exception e = response.exception;
        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof NotYourWpcException) throw (NotYourWpcException) e;
        }
    }


    //------------------------------- Notification Handler ------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        clientModel.update(null, notification);
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        clientModel.update(null, notification);
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        notification.username = clientModel.getUsername();
        clientModel.update(null, notification);
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        notification.username = clientModel.getUsername();
        clientModel.update(null, notification);
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        clientModel.update(null, notification);
    }
}
