package shared.network;

import shared.clientinfo.Position;

import shared.clientinfo.ToolCardInterruptValues;
import shared.exceptions.gameexceptions.CannotCreatePlayerException;
import shared.exceptions.gameexceptions.InvalidGameParametersException;
import shared.exceptions.gameexceptions.NotYourWpcException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;

import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.responses.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Contains all possible methods.
 */
public interface RemoteServer extends Remote {
    Response createUser(String username, String password, RemoteObserver observer) throws RemoteException, CannotRegisterUserException;

    Response login(String username, String password, RemoteObserver observer) throws RemoteException, CannotLoginUserException;

    Response findGame(String userToken, int numPlayers, int level) throws RemoteException, InvalidGameParametersException, CannotFindUserInDBException, CannotCreatePlayerException;

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

    Response interuptToolCard(String userToken, ToolCardInterruptValues value) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInterruptToolCardException, NoToolCardInUseException;

    Response cancelAction (String userToken) throws RemoteException, CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException;

    Response placeDice(String userToken, int id, Position position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    Response getNextMove(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException ;

    Response getUserStat(String userToken) throws RemoteException, CannotFindUserInDBException;

    void poll(String userToken) throws RemoteException;
    }

