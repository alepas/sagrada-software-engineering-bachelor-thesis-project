package client.network;

import shared.clientinfo.NextAction;
import shared.clientinfo.Position;
import shared.clientinfo.ToolCardInterruptValues;
import shared.exceptions.gameexceptions.CannotCreatePlayerException;
import shared.exceptions.gameexceptions.InvalidGameParametersException;
import shared.exceptions.gameexceptions.NotYourWpcException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;

import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ForceDisconnectionNotification;
import shared.network.commands.requests.*;
import shared.network.commands.responses.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient extends NetworkClient implements ResponseHandler {
    private final String host;
    private final int port;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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
                            observer.update(null, new ForceDisconnectionNotification(true));
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
                    Thread.currentThread().interrupt();
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

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((CreateUserResponse) response).exception;

        if (e != null){
            if (e instanceof CannotRegisterUserException) throw (CannotRegisterUserException) e;
        }
    }

    @Override
    public void login(String username, String password) throws CannotLoginUserException {
        request(new LoginRequest(username, password));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((LoginResponse) response).exception;

        if (e != null){
            if (e instanceof CannotLoginUserException) throw (CannotLoginUserException) e;
        }
    }

    @Override
    public void findGame(String token, int numPlayers, int level) throws CannotFindUserInDBException, InvalidGameParametersException, CannotCreatePlayerException {
        request(new FindGameRequest(token, numPlayers, level));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((FindGameResponse) response).exception;

        if (e != null){
            if (e instanceof CannotFindUserInDBException) throw (CannotFindUserInDBException) e;
            if (e instanceof InvalidGameParametersException) throw (InvalidGameParametersException) e;
            if (e instanceof CannotCreatePlayerException) throw (CannotCreatePlayerException) e;
        }
    }

    @Override
    public void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        request(new PickWpcRequest(wpcID, userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((PickWpcResponse) response).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof NotYourWpcException) throw (NotYourWpcException) e;
        }
    }

    @Override
    public void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        request(new PassTurnRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((PassTurnResponse) response).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotPerformThisMoveException) throw (CannotPerformThisMoveException) e;
        }
    }

    @Override
    public void getUserStat(String userToken) throws CannotFindUserInDBException {
        request(new GetUserStatRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((GetUserStatResponse) response).exception;

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
    public NextAction interruptToolCard(String userToken, ToolCardInterruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInterruptToolCardException, NoToolCardInUseException {
        request(new ToolCardInteruptRequest(userToken, value));

        ToolCardResponse response = (ToolCardResponse) waitResponse();
        assert response != null;
        Exception e = response.exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
            if (e instanceof PlayerNotAuthorizedException) throw (PlayerNotAuthorizedException) e;
            if (e instanceof CannotInterruptToolCardException) throw  (CannotInterruptToolCardException) e;
            if (e instanceof NoToolCardInUseException ) throw  (NoToolCardInUseException) e;
            return null;
        } else {
            return response.nextAction;
        }
    }


    @Override
    public void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedExtractedDicesRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((UpdatedExtractedDicesResponse) response).exception;

        if (e != null){
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;

        }

    }

    @Override
    public void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedPOCsRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((UpdatedPOCsResponse) response).exception;

        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedRoundTrackRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((UpdatedRoundTrackResponse) response).exception;

        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        request(new UpdatedToolCardsRequest(userToken));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((UpdatedToolCardsResponse) response).exception;

        if (e != null) {
            if (e instanceof CannotFindPlayerInDatabaseException) throw (CannotFindPlayerInDatabaseException) e;
        }
    }

    @Override
    public void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        request(new UpdatedWPCRequest(userToken, username));

        Response response = waitResponse();
        if (response == null) return;
        Exception e = ((UpdatedWPCResponse) response).exception;

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

    @Override
    public void logout() {
        try {
            close();
        } catch (IOException e) { /*Do nothing*/}
    }
}
