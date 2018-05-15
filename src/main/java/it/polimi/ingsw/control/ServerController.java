package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.commands.responses.PickWpcResponse;
import it.polimi.ingsw.control.network.socket.SocketClientHandler;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

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
}
