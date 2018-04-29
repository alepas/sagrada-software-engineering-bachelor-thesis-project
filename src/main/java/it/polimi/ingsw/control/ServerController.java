package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.socket.SocketClientHandler;
import it.polimi.ingsw.control.network.commands.RequestHandler;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;

public class ServerController implements RequestHandler {
    // reference to the networking layer
    private final SocketClientHandler clientHandler;

    // pieces of the model
    private final DatabaseUsers databaseUsers;

    public ServerController(SocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.databaseUsers = DatabaseUsers.getInstance();
    }

    public void displayText(String text){
        System.out.println(">>> " + text);
    }

    @Override
    public Response handle(CreateUserRequest request) {
//        String userToken = databaseUsers.registerUser(request.username, request.password);

        //Ipotizziamo che la creazione dell'utente sia andata a buon fine
        displayText("Creato l'utente: " + request.username);
        String userToken = "jhvhue7o%3u2hs";
        return new CreateUserResponse(request.username, userToken);
    }

    @Override
    public Response handle(LoginRequest request) {
//        String userToken = databaseUsers.login(request.username, request.password);

        //Ipotizziamo che il login sia andato a buon fine
        displayText("Login avvenuto: " + request.username);
        String userToken = "jhd739264%3£s";

        return new LoginResponse(request.username, userToken);
    }
}
