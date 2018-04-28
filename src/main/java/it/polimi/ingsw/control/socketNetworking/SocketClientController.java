package it.polimi.ingsw.control.socketNetworking;

import it.polimi.ingsw.control.socketNetworking.network.SagradaSocketClient;
import it.polimi.ingsw.control.socketNetworking.network.commands.ResponseHandler;
import it.polimi.ingsw.control.socketNetworking.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.socketNetworking.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.view.CliView;

public class SocketClientController implements ResponseHandler {
    // reference to networking layer
    private final SagradaSocketClient client;
    private Thread receiver;

    // the view
    private final CliView view;

    // Pieces of the model
    private String username;
    private String userToken;

    public SocketClientController(SagradaSocketClient client) {
        this.client = client;
        this.view = new CliView(this);
    }

    public void run(){
        view.createUsernamePhase();
    }

//    ------------------- Client methods ---------------------------
    public String createUser(String username, String password){
        client.request(new CreateUserRequest(username, password));
        client.nextResponse().handle(this);
        return this.username;
    }


//    ------------------ Response handling -------------------------
    @Override
    public void handle(CreateUserResponse response) {
        if(response.userToken == null){
            this.username = null;
            this.userToken = null;
            view.displayText("Unable to create user");
            return;
        }
        this.username = response.username;
        this.userToken = response.userToken;
    }
}
