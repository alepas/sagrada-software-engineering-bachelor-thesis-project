package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.clientModel.ToolCardInteruptValues;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Response createUser(String username, String password) throws RemoteException, CannotRegisterUserException;

    Response login(String username, String password) throws RemoteException, CannotLoginUserException;

    Response findGame(String userToken, int numPlayers, RemoteObserver observer) throws RemoteException, InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException;

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

    Response findAlreadyStartedGame(String userToken, RemoteObserver observer) throws RemoteException, CannotFindGameForUserInDatabaseException;

    }

