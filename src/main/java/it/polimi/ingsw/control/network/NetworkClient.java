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
import java.util.Observer;

public abstract class NetworkClient implements ResponseHandler {
    private static NetworkClient instance;
    protected final ClientModel clientModel = ClientModel.getInstance();
    protected Observer observer = ClientModel.getInstance();

    //TODO: Da eliminare signleton
    public static SocketClient getNewSocketInstance(String host, int port){
        instance = new SocketClient(host, port);
        return (SocketClient) instance;
    }

    public static RmiClient getNewRmiInstance() throws NotBoundException, RemoteException {
        instance = new RmiClient();
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

    public abstract void getUserStat(String userToken) throws CannotFindUserInDBException;



    public abstract NextAction useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException;

    public abstract NextAction pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    public abstract NextAction placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException ;

    public abstract NextAction pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException;

    //TODO
    public abstract NextAction interuptToolCard(String userToken, ToolCardInteruptValues value) throws  CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException;



    public abstract int findAlreadyStartedGame(String userToken) throws CannotFindGameForUserInDatabaseException;

    public abstract void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException;

    public abstract NextAction getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException;




    //TODO
    public abstract NextAction cancelAction (String userToken) throws  CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException;

    public abstract NextAction placeDice(String userToken, int id, Position position) throws  CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    //TODO
    public abstract NextAction getNextMove(String userToken) throws  CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException ;


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
            clientModel.exitGame();
            clientModel.setGame(new ClientGame(response.gameID, response.numPlayers));
//            clientModel.setGameID(response.gameID);
            clientModel.setGameActualPlayers(response.actualPlayers);
//            clientModel.setGameNumPlayers(response.numPlayers);
        }
    }

    @Override
    public void handle(PickWpcResponse response){

    }

    @Override
    public void handle(PassTurnResponse response){

    }

    @Override
    public void handle(ToolCardResponse response){
        if (response.exception == null){
            ToolCardClientNextActionInfo info = new ToolCardClientNextActionInfo(response.wherePickNewDice,
                response.wherePutNewDice, response.numbersToChoose, response.diceChosen, response.diceChosenLocation, response.stringForStopToolCard, response.bothYesAndNo, response.showBackButton);

            clientModel.setToolCardClientNextActionInfo(info);
            if (response.wpc != null) clientModel.setMyWpc(response.wpc);
            if (response.extractedDices != null) clientModel.setExtractedDices(response.extractedDices);
            if (response.roundTrack != null) clientModel.setRoundTrack(response.roundTrack);
        }
    }



    @Override
    public void handle(UpdatedExtractedDicesResponse response){
        if (response.exception == null) {
            clientModel.setExtractedDices(response.extractedDices);
        }
    }

    @Override
    public void handle(UpdatedGameResponse response){
        if (response.exception == null) {
            clientModel.setGame(response.game);
            clientModel.setActive(response.active);
            clientModel.setPrivateObjectives(response.privateObjectives);
            clientModel.setToolCardClientNextActionInfo(response.nextActionInfo);
            clientModel.setTimeLeftToCompleteTask(
                    (response.timeLeftToCompleteTask != null) ? response.timeLeftToCompleteTask : 0);
        }
    }

    @Override
    public void handle(UpdatedPOCsResponse response){
        if (response.exception == null) {
            clientModel.setGamePublicObjectiveCards(response.pocs);
        }
    }

    @Override
    public void handle(UpdatedRoundTrackResponse response){
        if (response.exception == null) {
            clientModel.setRoundTrack(response.roundTrack);
        }
    }

    @Override
    public void handle(UpdatedToolCardsResponse response){
        if (response.exception == null) {
            clientModel.setGameToolCards(response.toolCards);
        }
    }

    @Override
    public void handle(UpdatedWPCResponse response){
        if (response.exception == null) {
            clientModel.getWpcByUsername().put(response.user,response.wpc);
        }
    }

    @Override
    public void handle(PlaceDiceResponse response) {

    }

    @Override
    public void handle(NextMoveResponse response) {

    }

    @Override
    public void handle(GetUserStatResponse response) {
        if (response.exception != null){
            clientModel.setUser(response.user);
        }
    }
}
