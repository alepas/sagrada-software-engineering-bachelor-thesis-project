package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.commands.requests.FindGameRequest;
import it.polimi.ingsw.control.network.commands.responses.FindGameResponse;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.requests.LoginRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.control.network.commands.responses.LoginResponse;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.userExceptions.NullTokenException;
import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.view.CliView;

public class ClientController implements ResponseHandler {
    // reference to networking layer
    private final SocketClient client;
    private Thread receiver;

    // the view
    private final CliView view;

    // Pieces of the model
    private String username;
    private String userToken;

    public ClientController(SocketClient client) {
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
        client.request(new CreateUserRequest(username, password));
        client.nextResponse().handle(this);
        return this.username;
    }

    public String login(String username, String password){
        client.request(new LoginRequest(username, password));
        client.nextResponse().handle(this);
        return this.username;
    }

    public void findGame(int numPlayers) {
        client.request(new FindGameRequest(userToken, numPlayers));
        client.nextResponse().handle(this);
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
}
