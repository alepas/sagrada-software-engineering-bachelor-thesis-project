package it.polimi.ingsw.control.network.socket;

import it.polimi.ingsw.control.ServerController;
import it.polimi.ingsw.control.network.commands.requests.*;
import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class SocketClientHandler implements Runnable, Observer, RequestHandler {
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean stop;

    private final ServerController controller;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        this.controller = new ServerController(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public void respond(Object response) {
        try {
            out.writeObject(response);
        } catch (IOException e) {
            printError("IO - " + e.getMessage());
        }
    }

    private void printError(String message) {
        System.err.println(">>> ERROR@" + socket.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void run() {
        try {
            do {
                Response response = ((Request) in.readObject()).handle(this);
                if (response != null) {
                    respond(response);
                }
            } while (!stop);
        } catch (Exception e) {
//            printError(e.getClass().getSimpleName() + " - " + e.getMessage());
            close();
        }

        close();
    }

    public void stop(){ stop = true; }

    private void close(){
        stop = true;
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                printError("Errors in closing - " + e.getMessage());
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                printError("Errors in closing - " + e.getMessage());
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            printError("Errors in closing - " + e.getMessage());
        }
    }


    //------------------------------ Request handler ------------------------------

    @Override
    public Response handle(CreateUserRequest request){
        try {
            return controller.createUser(request.username, request.password, socket);
        } catch (CannotRegisterUserException e){
            return new CreateUserResponse(request.username, null, e);
        }
    }

    @Override
    public Response handle(LoginRequest request) {
        try {
            return controller.login(request.username, request.password, socket);
        } catch (CannotLoginUserException e){
            return new LoginResponse(request.username, null, e);
        }
    }

    @Override
    public Response handle(FindGameRequest request) {
        try {
            return controller.findGame(request.token, request.numPlayers, this);
        } catch (InvalidNumOfPlayersException|CannotFindUserInDBException|CannotCreatePlayerException e){
            return new FindGameResponse(null, 0, 0, e);
        }
    }

    @Override
    public Response handle(PickWpcRequest request) {
        try {
            return controller.pickWpc(request.userToken, request.wpcID);
        } catch (CannotFindPlayerInDatabaseException |NotYourWpcException e) {
            return new PickWpcResponse(e);
        }
    }

    @Override
    public Response handle(PassTurnRequest request) {
        try {
            return controller.passTurn(request.userToken);
        } catch (CannotFindPlayerInDatabaseException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
            return new PassTurnResponse(e);
        }
    }



    @Override
    public Response handle(PickDiceRequest request) {
        try{
            return controller.pickDice(request.userToken,request.diceId);
        } catch (PlayerNotAuthorizedException | CannotFindPlayerInDatabaseException | CannotPickDiceException |CannotPerformThisMoveException e) {
            return new PickDiceResponse(e);
        }
    }

    @Override
    public Response handle(PickPositionRequest request) {
        try {
            return controller.pickPosition(request.userToken, request.position);
        } catch (PlayerNotAuthorizedException | CannotFindPlayerInDatabaseException | CannotPickPositionException|CannotPerformThisMoveException e) {
            return new PickPositionResponse(e);
        }
    }

    @Override
    public Response handle(UseToolCardRequest request) {
        return null;
    }

    @Override
    public Response handle(UpdatedRoundTrackRequest updatedRoundTrackRequest) {
        return null;
    }

    @Override
    public Response handle(UpdatedExtractedDicesRequest updatedExtractedDicesRequest) {
        return null;
    }

    @Override
    public Response handle(UpdatedPOCsRequest updatedPOCsRequest) {
        return null;
    }

    @Override
    public Response handle(UpdatedToolCardsRequest updatedToolCardsRequest) {
        return null;
    }

    @Override
    public Response handle(UpdatedWPCRequest updatedWPCRequest) {
        return null;
    }

    @Override
    public Response handle(UpdatedGameRequest updatedGameRequest) {
        return null;
    }


    @Override
    public Response handle(ToolCardPickColorRequest request) {
        return null;
    }

    @Override
    public Response handle(ToolCardPickDiceRequest request) {
        return null;
    }

    @Override
    public Response handle(ToolCardPickNumberRequest request) {
        return null;
    }

    @Override
    public Response handle(ToolCardPickPositionRequest request) {
        return null;
    }


    //------------------------------ Game observer ------------------------------

    @Override
    public void update(Observable o, Object arg) {
        respond(arg);
    }
}
