package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.requests.*;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.clientModel.ClientPosition;
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
                        } catch (Exception e){
                            e.printStackTrace();
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
    public void pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PickDiceRequest(userToken, diceId));

        Exception e = ((PickDiceResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
        }

    }

    @Override
    public void pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PickPositionRequest(userToken, position));

        Exception e = ((PickPositionResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickPositionException ) throw  (CannotPickPositionException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;

        }
    }


    @Override
    public void useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        request(new UseToolCardRequest(userToken, cardId));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotUseToolCardException ) throw  (CannotUseToolCardException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;

        }
    }

    @Override
    public void pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        request(new ToolCardPickDiceRequest(userToken, diceId,where));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;

        }
    }

    @Override
    public void placeDiceForToolCard(String userToken, int diceId, ClientDiceLocations diceFrom, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        request(new ToolCardPlaceDiceRequest(userToken, diceId,diceFrom,position));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickPositionException ) throw  (CannotPickPositionException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;


        }
    }

    @Override
    public void pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException, CannotPerformThisMoveException {
        request(new ToolCardPickColorRequest(userToken, color));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickColorException ) throw  (CannotPickColorException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;

        }
    }

    @Override
    public void pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        request(new ToolCardPickNumberRequest(userToken, number));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickNumberException ) throw  (CannotPickNumberException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;

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
    public void getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedGameRequest(userToken));
        Exception e = ((UpdatedGameResponse) waitResponse()).exception;
        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void stopToolCard(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException {
        request(new StopToolCardRequest(userToken));
        Exception e = ((MoveResponse)waitResponse()).exception;
        if (e!=null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotStopToolCardException ) throw  (CannotStopToolCardException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
        }
    }

    @Override
    public void cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        request(new CancelActionRequest(userToken));
        Exception e = ((MoveResponse)waitResponse()).exception;
        if (e!=null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotCancelActionException ) throw  (CannotCancelActionException) e;
        }

    }

    @Override
    public void placeDice(String userToken, int id, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PlaceDiceRequest(userToken, id, position));

        Exception e = ((MoveResponse) waitResponse()).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPickPositionException ) throw  (CannotPickPositionException) e;
            if (e instanceof CannotPerformThisMoveException ) throw  (CannotPerformThisMoveException) e;
            if (e instanceof CannotPickDiceException ) throw  (CannotPickDiceException) e;
        }
    }
}
