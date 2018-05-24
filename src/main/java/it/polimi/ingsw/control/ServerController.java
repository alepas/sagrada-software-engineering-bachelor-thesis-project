package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.control.network.socket.SocketClientHandler;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

import java.net.Socket;
import java.util.Observer;

public class ServerController {
    // pieces of the model
    private final DatabaseUsers databaseUsers;
    private final DatabaseGames databaseGames;

    public ServerController(SocketClientHandler clientHandler) {
        this.databaseUsers = DatabaseUsers.getInstance();
        this.databaseGames = DatabaseGames.getInstance();
    }

    public void displayText(String text){
        System.out.println(">>> " + text);
    }

    public Response createUser(String username, String password, Socket socket) throws CannotRegisterUserException {
        String userToken = databaseUsers.registerUser(username, password, socket);
        displayText("Creato l'utente: " + username);
        return new CreateUserResponse(username, userToken, null);
    }

    public Response login(String username, String password, Socket socket) throws CannotLoginUserException {
        String userToken = databaseUsers.login(username, password, socket);
        displayText("Login avvenuto: " + username);
        return new LoginResponse(username, userToken, null);
    }

    public Response findGame(String userToken, int numPlayers, Observer observer) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException {
        String username = databaseUsers.getUsernameByToken(userToken);
        Game game = databaseGames.findGameForUser(username, numPlayers);

        game.addObserver(observer);

        return new FindGameResponse(game.getID(), game.numActualPlayers(), game.getNumPlayers(), null);
    }

    public Response pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Game game = player.getGame();
        game.setPlayerWpc(player, wpcID);

        return new PickWpcResponse(null);
    }

    public Response passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        databaseUsers.getPlayerInGameFromToken(userToken).endTurn();
        return new PassTurnResponse(null);
    }

    public Response setToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.useToolCard(cardId);
    }

    public Response pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.pickDiceforToolCard(diceId,where);
    }

    public Response pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos=new Position(position.getRow(),position.getColumn());
        return player.pickPositionForToolCard(pos);
    }

    public Response pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Color colorTemp=Color.getColorFromClientColor(color);
        return player.pickColorForToolCard(colorTemp);
    }

    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.pickNumberForToolCard(number);
    }

    public Response pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        player.pickDice(diceId);
        return new PickDiceResponse(null);
    }

    public Response pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickPositionException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos=new Position(position.getRow(),position.getColumn());
        player.pickPosition(pos);
        return new PickPositionResponse(null);
    }

}
