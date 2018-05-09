package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.RequestHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.control.network.socket.SocketClientHandler;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;

public class ServerController {
    private final SocketClientHandler clientHandler;

    // pieces of the model
    private final DatabaseUsers databaseUsers;
    private final DatabaseGames databaseGames;

    public ServerController(SocketClientHandler clientHandler) {
        this.databaseUsers = DatabaseUsers.getInstance();
        this.databaseGames = DatabaseGames.getInstance();
        this.clientHandler = clientHandler;
    }

    public void displayText(String text){
        System.out.println(">>> " + text);
    }

    public Response createUser(String username, String password) throws CannotRegisterUserException {
        String userToken = databaseUsers.registerUser(username, password);
        displayText("Creato l'utente: " + username);
        return new CreateUserResponse(username, userToken, null);
    }

    public Response login(String username, String password) throws CannotLoginUserException {
        String userToken = databaseUsers.login(username, password);
        displayText("Login avvenuto: " + username);
        return new LoginResponse(username, userToken, null);
    }

    public Response findGame(String userToken, int numPlayers) throws InvalidPlayersException, NullTokenException, CannotFindUserInDBException {
        String username = databaseUsers.getUsernameByToken(userToken);
        Game game = databaseGames.findGameForUser(username, numPlayers);

        if (clientHandler != null) { game.addObserver(clientHandler); }

        return new FindGameResponse(game.getID(), game.numActualPlayers(), game.getNumPlayers(), null);
    }
}
