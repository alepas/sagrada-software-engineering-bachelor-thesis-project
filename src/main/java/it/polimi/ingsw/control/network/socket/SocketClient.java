package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.requests.*;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.clientModel.NextAction;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.clientModel.ToolCardInteruptValues;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
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
    
    private void startReceiving() {
        receiver = new Thread(
                () -> {
                    Response response = null;
                    do {
                        try {
                            Object obj = in.readObject();
                            if (obj instanceof Response){
                                response = (Response) obj;
                                response.handle(this);
                                lastResponse = response;
                                notifyResponse();
                            } else if (obj != null){
                                observer.update(null, obj);
                            }
                        } catch (IOException|ClassNotFoundException e){
                            //TODO: Patch momentanea, bisognerebbe far printare il messaggio alla view
                            System.out.println(">>> Connessione con il server persa");
                            return;
                        }
                    } while (true);
                }
        );
        receiver.start();
    }

    private Response waitResponse(){
        synchronized (responseWaiter){
            while (lastResponse == null) {
                try {
                    responseWaiter.wait();
                    Response response = lastResponse;
                    lastResponse = null;
                    return response;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private void notifyResponse(){
        synchronized (responseWaiter){
            responseWaiter.notifyAll();
        }
    }


    //-------------------------------- NetworkClientMethods --------------------------------

    @Override
    public void createUser(String username, String password) throws CannotRegisterUserException {
        request(new CreateUserRequest(username, password));

        Exception e = ((CreateUserResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotRegisterUserException) throw (CannotRegisterUserException) e;
        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        request(new LoginRequest(username, password));

        Exception e = ((LoginResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotLoginUserException) throw (CannotLoginUserException) e;
        }
    }

    @Override
    public void findGame(String token, int numPlayers) throws CannotFindUserInDBException, InvalidNumOfPlayersException, CannotCreatePlayerException {
        request(new FindGameRequest(token, numPlayers));

        Exception e = ((FindGameResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindUserInDBException) throw (CannotFindUserInDBException) e;
            if (e instanceof InvalidNumOfPlayersException) throw (InvalidNumOfPlayersException) e;
            if (e instanceof CannotCreatePlayerException) throw (CannotCreatePlayerException) e;
        }
    }

    @Override
    public void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        request(new PickWpcRequest(wpcID, userToken));

        Exception e = ((PickWpcResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof NotYourWpcException) throw (NotYourWpcException) e;
        }
    }

    @Override
    public void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PassTurnRequest(userToken));

        Exception e = ((PassTurnResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPerformThisMoveException) throw (CannotPerformThisMoveException) e;
        }
    }

    @Override
    public void getUserStat(String userToken) throws CannotFindUserInDBException {
        request(new GetUserStatRequest(userToken));

        Exception e = ((GetUserStatResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindUserInDBException) throw (CannotFindUserInDBException) e;
        }
    }


    @Override
    public NextAction useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        request(new ToolCardUseRequest(userToken, cardId));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotUseToolCardException ) throw  (CannotUseToolCardException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }

    @Override
    public NextAction pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        request(new ToolCardPickDiceRequest(userToken, diceId));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }

    @Override
    public NextAction placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        request(new ToolCardPlaceDiceRequest(userToken, diceId, position));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
            if (e instanceof CannotPickPositionException ) throw  (CannotPickPositionException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }



    @Override
    public NextAction pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        request(new ToolCardPickNumberRequest(userToken, number));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickNumberException ) throw  (CannotPickNumberException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }

    @Override
    public NextAction interuptToolCard(String userToken, ToolCardInteruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException {
        request(new ToolCardInteruptRequest(userToken, value));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotInteruptToolCardException) throw  (CannotInteruptToolCardException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }


    @Override
    public void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedExtractedDicesRequest(userToken));

        Exception e = ((UpdatedExtractedDicesResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;

        }

    }

    @Override
    public void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedPOCsRequest(userToken));
        Exception e = ((UpdatedPOCsResponse) waitResponse()).exception;
        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedRoundTrackRequest(userToken));
        Exception e = ((UpdatedRoundTrackResponse) waitResponse()).exception;
        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedToolCardsRequest(userToken));
        Exception e = ((UpdatedToolCardsResponse) waitResponse()).exception;
        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        request(new UpdatedWPCRequest(userToken, username));
        Exception e = ((UpdatedWPCResponse) waitResponse()).exception;
        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof UserNotInThisGameException) throw (UserNotInThisGameException) e;
        }
    }

    @Override
    public NextAction getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedGameRequest(userToken));

        UpdatedGameResponse response = (UpdatedGameResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }


    @Override
    public int findAlreadyStartedGame(String userToken) throws CannotFindGameForUserInDatabaseException {
        request(new FindAlreadyStartedGameRequest(userToken));

        UpdatedGameResponse response=(UpdatedGameResponse) waitResponse();
        assert response !=null;
        Exception e = response.exception;

        if (e!=null){
            if (e instanceof CannotFindGameForUserInDatabaseException) throw (CannotFindGameForUserInDatabaseException)e;
            return 0;
        }
        else
            return response.gameNumPlayers;
    }

    @Override
    public NextAction cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        request(new CancelActionRequest(userToken));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotCancelActionException ) throw  (CannotCancelActionException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }

    @Override
    public NextAction placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PlaceDiceRequest(userToken, id, position));

        PlaceDiceResponse response = (PlaceDiceResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickPositionException ) throw  (CannotPickPositionException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }

    @Override
    public NextAction getNextMove(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        return null;
    }
}
