package it.polimi.ingsw.control.socketNetworking;

import it.polimi.ingsw.control.socketNetworking.network.SocketClientHandler;
import it.polimi.ingsw.control.socketNetworking.network.commands.RequestHandler;
import it.polimi.ingsw.control.socketNetworking.network.commands.Response;
import it.polimi.ingsw.control.socketNetworking.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.socketNetworking.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;

public class SocketServerController implements RequestHandler {
    // reference to the networking layer
    private final SocketClientHandler clientHandler;

    // pieces of the model
    private final DatabaseUsers databaseUsers;

    public SocketServerController(SocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.databaseUsers = DatabaseUsers.getInstance();
    }

    @Override
    public Response handle(CreateUserRequest request) {
//        String userToken = databaseUsers.registerUser(request.username, request.password);

        //Ipotizziamo che la creazione dell'utente sia andata a buon fine
        System.out.println("Creato l'utente: " + request.username);
        String userToken = "jhvhue7o%3u2hs";
        return new CreateUserResponse(request.username, userToken);
    }
}
