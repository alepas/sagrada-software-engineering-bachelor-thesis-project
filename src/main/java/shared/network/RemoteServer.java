package shared.network;

import shared.clientInfo.Position;
import shared.clientInfo.ToolCardInteruptValues;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidNumOfPlayersException;
import shared.exceptions.gameExceptions.NotYourWpcException;
import shared.exceptions.gameExceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.responses.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response createUser(String username, String password, RemoteObserver observer) throws RemoteException, CannotRegisterUserException;

    Response login(String username, String password, RemoteObserver observer) throws RemoteException, CannotLoginUserException;

    Response findGame(String userToken, int numPlayers, int level) throws RemoteException, InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

    Response pickWpc(String userToken, String wpcID) throws RemoteException, CannotFindPlayerInDatabaseException, NotYourWpcException;

    Response passTurn(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    Response useToolCard(String userToken, String cardId) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException;

    Response pickDiceForToolCard(String userToken, int diceId) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    Response placeDiceForToolCard(String userToken, int diceId, Position position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException,CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    Response pickNumberForToolCard(String userToken, int number) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException;

    Response getUpdatedExtractedDices(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedPOCs(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedRoundTrack(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedToolCards(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedWPC(String userToken, String username) throws RemoteException, CannotFindPlayerInDatabaseException, UserNotInThisGameException ;

    Response getUpdatedGame(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response interuptToolCard(String userToken, ToolCardInteruptValues value) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException;

    Response cancelAction (String userToken) throws RemoteException, CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException;

    Response placeDice(String userToken, int id, Position position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    Response getNextMove(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException ;

    Response getUserStat(String userToken) throws RemoteException, CannotFindUserInDBException;

    void poll(String userToken) throws RemoteException;
    }

