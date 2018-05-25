package it.polimi.ingsw.control.network;

import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.control.network.rmi.RmiClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class NetworkClient implements ResponseHandler {
    private static NetworkClient instance;
    protected final ClientModel clientModel = ClientModel.getInstance();

    public static SocketClient getNewSocketInstance(String host, int port){
        if (instance == null){
            instance = new SocketClient(host, port);
        }
        return (SocketClient) instance;
    }

    public static RmiClient getNewRmiInstance() throws NotBoundException, RemoteException {
        if (instance == null){
            instance = new RmiClient();
        }
        return (RmiClient) instance;
    }

    public static NetworkClient getInstance(){
        return instance;
    }

    public abstract void createUser(String username, String password) throws CannotRegisterUserException;

    public abstract void login(String username, String password) throws CannotLoginUserException;

    public abstract void findGame(String token, int numPlayers) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

    public abstract void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException;

    public abstract void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    public abstract void useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException;

    public abstract void pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException;

    public abstract void pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException;

    public abstract void pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException;

    public abstract void pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException;

    public abstract void pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    public abstract void pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    public abstract void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException;

    public abstract void getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException;










    //-------------------------------- Response Handler --------------------------------

    @Override
    public void handle(CreateUserResponse response){
        if (response.exception == null) {
            clientModel.setUsername(response.username);
            clientModel.setUserToken(response.userToken);
        }
    }

    @Override
    public void handle(LoginResponse response){
        if (response.exception == null) {
            clientModel.setUsername(response.username);
            clientModel.setUserToken(response.userToken);
        }
    }

    @Override
    public void handle(FindGameResponse response){
        if (response.exception == null) {
            clientModel.setGameID(response.gameID);
            clientModel.setGameActualPlayers(response.actualPlayers);
            clientModel.setGameNumPlayers(response.numPlayers);
        }
    }

    @Override
    public void handle(PickWpcResponse response){

    }

    @Override
    public void handle(ToolCardResponse response){

    }

    @Override
    public void handle(PickDiceResponse response){

    }

    @Override
    public void handle(PickPositionResponse response){

    }

    @Override
    public void handle(PassTurnResponse response){

    }

    @Override
    public void handle(UpdatedExtractedDicesResponse response){

    }

    @Override
    public void handle(UpdatedGameResponse response){

    }

    @Override
    public void handle(UpdatedPOCsResponse response){

    }

    @Override
    public void handle(UpdatedRoundTrackResponse response){

    }

    @Override
    public void handle(UpdatedToolCardsResponse response){

    }

    @Override
    public void handle(UpdatedWPCResponse response){

    }

}
