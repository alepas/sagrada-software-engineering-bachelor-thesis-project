package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.GenericErrorResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.view.CliView;

public class ClientController implements ResponseHandler {
    // reference to networking layer
    private final NetworkClient client;

    // the view
    private final CliView view;

    // Pieces of the model
    private String username;
    private String userToken;

    public ClientController(NetworkClient client) {
        this.client = client;
        this.view = new CliView(this);
    }

    public void run(){
        do {
            if (view.logPhase()) view.login();
            else view.createUsername();
        } while (userToken == null);

        //---------- L'utente è ora loggato ----------

        view.mainMenuPhase();
    }

    //    ------------------- Client methods ---------------------------
    public String createUser(String username, String password){
        Request request = new CreateUserRequest(username, password);
        client.request(request).handle(this);
        return this.username;
    }

    public String login(String username, String password){
        Request request = new LoginRequest(username, password);
        client.request(request).handle(this);
        return this.username;
    }

    public void findGame(int numPlayers) {
        Request request = new FindGameRequest(userToken, numPlayers);
        client.request(request).handle(this);
    }


    //    ------------------ Response handling -------------------------
    public void cleanUser(){
        this.username = null;
        this.userToken = null;
    }

    @Override
    public void handle(CreateUserResponse response) {
        if(response.userToken == null){
            cleanUser();
            view.displayText(response.error);
            return;
        }
        this.username = response.username;
        this.userToken = response.userToken;
    }

    @Override
    public void handle(LoginResponse response) {
        if(response.userToken == null){
            cleanUser();
            view.displayText(response.error);
            return;
        }
        this.username = response.username;
        this.userToken = response.userToken;
    }

    @Override
    public void handle(FindGameResponse response) {
        String gameID = response.gameID;
        if (gameID != null) {
            view.displayText("Aggiunto alla partita: " + gameID);
        } else {
            view.displayText(response.error);
        }
    }

    @Override
    public void handle(GenericErrorResponse response) {
        view.displayText(response.error);
    }
}
