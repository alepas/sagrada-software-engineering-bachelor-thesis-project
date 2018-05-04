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

public class ServerController implements RequestHandler {
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

    @Override
    public Response handle(CreateUserRequest request) {
        String userToken = null;
        String error = null;

        try {
            userToken = databaseUsers.registerUser(request.username, request.password);
            displayText("Creato l'utente: " + request.username);
//            databaseUsers.setSocketForUser(userToken, clientHandler.getSocket());
        } catch (CannotRegisterUserException e){
            displayText(e.getMessage());
            error = e.getMessage();
        }

        return new CreateUserResponse(request.username, userToken, error);
    }

    @Override
    public Response handle(LoginRequest request) {
        String userToken = null;
        String error = null;

        try {
            userToken = databaseUsers.login(request.username, request.password);
            displayText("Login avvenuto: " + request.username);
        } catch (CannotLoginUserException e){
            displayText(e.getMessage());
            error = e.getMessage();
        }

        return new LoginResponse(request.username, userToken, error);
    }

    @Override
    public Response handle(FindGameRequest request) {
        String username = null;
        try {
            username = databaseUsers.getUsernameByToken(request.token);
            Game game = databaseGames.findGameForUser(username, request.numPlayers);

            if (clientHandler != null) { game.addObserver(clientHandler); }

            return new FindGameResponse(game, null);

        } catch (NullTokenException e) {
            return new FindGameResponse(null, e.getMessage());
        } catch (CannotFindUserInDBException e) {
            return new FindGameResponse(null, e.getMessage());
        } catch (InvalidPlayersException e){
            return new FindGameResponse(null, e.getMessage());
        } catch (Exception e){
            return new FindGameResponse(null, e.getMessage());
        }
    }
}
