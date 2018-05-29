package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
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

    Response pickDice(String userToken, int diceId) throws RemoteException, CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    Response pickPosition(String userToken, ClientPosition position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, CannotPerformThisMoveException;

    Response useToolCard(String userToken, String cardId) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException;

    Response pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    Response placeDiceForToolCard(String userToken, int diceId, ClientDiceLocations diceFrom, ClientPosition position) throws RemoteException, CannotFindPlayerInDatabaseException, CannotPickPositionException,CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException;

    Response pickColorForToolCard(String userToken, ClientColor color) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException, CannotPerformThisMoveException;

    Response pickNumberForToolCard(String userToken, int number) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException;

    Response getUpdatedExtractedDices(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedPOCs(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedRoundTrack(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedToolCards(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response getUpdatedWPC(String userToken, String username) throws RemoteException, CannotFindPlayerInDatabaseException, UserNotInThisGameException ;

    Response getUpdatedGame(String userToken) throws RemoteException, CannotFindPlayerInDatabaseException;

    Response stopToolCard (String userToken) throws RemoteException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException;

    Response cancelAction (String userToken) throws RemoteException, CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException;














    }

