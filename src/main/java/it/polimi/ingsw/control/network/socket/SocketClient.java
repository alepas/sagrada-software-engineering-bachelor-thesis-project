package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observer;

public class SocketClient implements NetworkClient, ResponseHandler {
    private final String host;
    private final int port;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientModel clientModel = ClientModel.getInstance();
    private Observer observer = ClientModel.getInstance();

    private Thread receiver;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        connection = new Socket(host, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    public Response request(Request request) {
        try {
            out.writeObject(request);
            return ((Response) in.readObject());
        } catch (IOException e) {
            throw new RuntimeException("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Wrong deserialization: " + e.getMessage());
        }
    }

    @Override
    public void startPlaying() {
        receiver = new Thread(
                () -> {
                    Response response = null;
                    do {
                        try {
                            response = (Response) in.readObject();
                            if (response != null) {
                                response.handle(this);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } while (response != null);
                }
        );
        receiver.start();
    }




    //-------------------------------- NetworkClientMethods --------------------------------

    @Override
    public void createUser(String username, String password) throws CannotRegisterUserException {
        CreateUserResponse response = (CreateUserResponse) request(new CreateUserRequest(username, password));
        response.handle(this);
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        LoginResponse response = (LoginResponse) request(new LoginRequest(username, password));
        response.handle(this);
    }

    @Override
    public void findGame(String token, int numPlayers) throws InvalidPlayersException, NullTokenException, CannotFindUserInDBException {
        FindGameResponse response = (FindGameResponse) request(new FindGameRequest(token, numPlayers));
        response.handle(this);
    }



    //-------------------------------- Response Handler --------------------------------

    @Override
    public void handle(CreateUserResponse response) throws CannotRegisterUserException {
        if(response.userToken == null){
            clientModel.clean();
            if (response.exception instanceof CannotRegisterUserException) throw (CannotRegisterUserException) response.exception;
//            view.displayText(response.exception);         //TODO: fare exception
            return;
        }
        clientModel.setUsername(response.username);
        clientModel.setUserToken(response.userToken);
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
    public void handle(FindGameResponse response) throws InvalidPlayersException, NullTokenException, CannotFindUserInDBException {
        String gameID = response.gameID;
        if (gameID != null) {
            clientModel.setGameID(gameID);
            clientModel.setGameActualPlayers(response.actualPlayers);
            clientModel.setGameNumPlayers(response.numPlayers);
            startPlaying();
        } else {
            if (response.exception instanceof InvalidPlayersException) throw (InvalidPlayersException) response.exception;
            if (response.exception instanceof NullTokenException) throw (NullTokenException) response.exception;
            if (response.exception instanceof CannotFindUserInDBException) throw (CannotFindUserInDBException) response.exception;
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
        ((PrivateObjExtractedNotification) notification).username = clientModel.getUsername();
        clientModel.update(null, notification);
    }
}
