package client.network;

import shared.clientInfo.*;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidGameParametersException;
import shared.exceptions.gameExceptions.NotYourWpcException;
import shared.exceptions.gameExceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.responses.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observer;

public abstract class NetworkClient implements ResponseHandler {
    private static NetworkClient instance;
    protected final ClientInfo clientInfo = ClientInfo.getInstance();
    protected Observer observer = ClientInfo.getInstance();

    /**
     * @param host is the server address
     * @param port is the server socket door
     * @return the instance to this
     */
    public static SocketClient getNewSocketInstance(String host, int port){
        instance = new SocketClient(host, port);
        return (SocketClient) instance;
    }

    /**
     * @return the instance of this
     * @throws NotBoundException if an attempt is made to lookup or unbind in the registry a name that has
     *          no associated binding.
     * @throws RemoteException if it not posible to create an rmi connection
     */
    public static RmiClient getNewRmiInstance() throws NotBoundException, RemoteException {
        instance = new RmiClient();
        return (RmiClient) instance;
    }

    /**
     * @return this instance
     */
    public static NetworkClient getInstance(){
        return instance;
    }



    public abstract void createUser(String username, String password) throws CannotRegisterUserException;

    public abstract void login(String username, String password) throws CannotLoginUserException;

    public abstract void findGame(String token, int numPlayers, int level) throws InvalidGameParametersException, CannotFindUserInDBException, CannotCreatePlayerException;

    public abstract void pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException;

    public abstract void passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    public abstract void getUserStat(String userToken) throws CannotFindUserInDBException;



    public abstract NextAction useToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException;

    public abstract NextAction pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    public abstract NextAction placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException ;

    public abstract NextAction pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException;

    public abstract NextAction interruptToolCard(String userToken, ToolCardInteruptValues value) throws  CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInterruptToolCardException, NoToolCardInUseException;



    public abstract void getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException;

    public abstract void getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException;

    public abstract NextAction getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException;





    public abstract NextAction cancelAction (String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException;

    public abstract NextAction placeDice(String userToken, int id, Position position) throws  CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException;


    public abstract NextAction getNextMove(String userToken) throws  CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException ;


    //-------------------------------- Response Handler --------------------------------

    @Override
    public void handle(CreateUserResponse response){
        if (response.exception == null) {
            clientInfo.setUsername(response.username);
            clientInfo.setUserToken(response.userToken);
        }
    }

    @Override
    public void handle(LoginResponse response){
        if (response.exception == null) {
            clientInfo.setUsername(response.username);
            clientInfo.setUserToken(response.userToken);
        }
    }

    @Override
    public void handle(FindGameResponse response){
        if (response.exception == null) {
            clientInfo.exitGame();
            clientInfo.setGame(new ClientGame(response.gameID, response.numPlayers));
            clientInfo.setGameActualPlayers(response.actualPlayers);
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

            clientInfo.setToolCardClientNextActionInfo(info);
            if (response.wpc != null) clientInfo.setMyWpc(response.wpc);
            if (response.extractedDices != null) clientInfo.setExtractedDices(response.extractedDices);
            if (response.roundTrack != null) clientInfo.setRoundTrack(response.roundTrack);
        }
    }



    @Override
    public void handle(UpdatedExtractedDicesResponse response){
        if (response.exception == null) {
            clientInfo.setExtractedDices(response.extractedDices);
        }
    }

    @Override
    public void handle(UpdatedGameResponse response){
        if (response.exception == null) {
            clientInfo.setGame(response.game);
            clientInfo.setActive(response.active);
            clientInfo.setPrivateObjectives(response.privateObjectives);
            clientInfo.setToolCardClientNextActionInfo(response.nextActionInfo);
            clientInfo.setTimeLeftToCompleteTask(
                    (response.timeLeftToCompleteTask != null) ? response.timeLeftToCompleteTask : 0);
            clientInfo.setWpcsArrived(true);
        }
    }

    @Override
    public void handle(UpdatedPOCsResponse response){
        if (response.exception == null) {
            clientInfo.setGamePublicObjectiveCards(response.pocs);
        }
    }

    @Override
    public void handle(UpdatedRoundTrackResponse response){
        if (response.exception == null) {
            clientInfo.setRoundTrack(response.roundTrack);
        }
    }

    @Override
    public void handle(UpdatedToolCardsResponse response){
        if (response.exception == null) {
            clientInfo.setGameToolCards(response.toolCards);
        }
    }

    @Override
    public void handle(UpdatedWPCResponse response){
        if (response.exception == null) {
            clientInfo.getWpcByUsername().put(response.user,response.wpc);
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
        if (response.exception == null){
            clientInfo.setUser(response.user);
        }
    }
}
