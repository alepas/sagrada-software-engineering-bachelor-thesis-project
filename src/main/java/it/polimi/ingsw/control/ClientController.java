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
import it.polimi.ingsw.control.network.rmi.RmiClient;
import it.polimi.ingsw.control.network.socket.SocketClient;
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.view.CliView;

public class ClientController implements ResponseHandler {
    // reference to networking layer
    private final NetworkClient client;

    private static ClientController instance;

    // the view
    private final CliView view;

    // Pieces of the model
    private ClientContext clientContext;

    public static ClientController getInstance(NetworkClient client) {
        if (instance == null){
            instance = new ClientController(client);
        }
        return instance;
    }

    public static ClientController getInstance() {
        return instance;
    }

    private ClientController(NetworkClient client) {
        this.client = client;
        this.clientContext = ClientContext.get();
        this.view = new CliView(this);
    }

    public void run(){
        do {
            if (view.logPhase()) view.login();
            else view.createUsername();
        } while (clientContext.getUserToken() == null);

        //---------- L'utente è ora loggato ----------

        view.mainMenuPhase();
    }



    //    ------------------- Client methods ---------------------------

    public void startPlaying(){
        client.startPlaying(this, clientContext.getCurrentGame().getID());
    }

    public String createUser(String username, String password){
        Request request = new CreateUserRequest(username, password);
        client.request(request).handle(this);
        return clientContext.getUsername();
    }

    public String login(String username, String password){
        Request request = new LoginRequest(username, password);
        client.request(request).handle(this);
        return clientContext.getUsername();
    }

    public Game findGame(int numPlayers) {
        Request request = null;
        request = new FindGameRequest(clientContext.getUserToken(), numPlayers);
        client.request(request).handle(this);
        return clientContext.getCurrentGame();
    }


    //    ------------------ Response handling -------------------------

    @Override
    public void handle(CreateUserResponse response) {
        if(response.userToken == null){
            clientContext.clean();
            view.displayText(response.error);
            return;
        }
        clientContext.setUsername(response.username);
        clientContext.setUserToken(response.userToken);
    }

    @Override
    public void handle(LoginResponse response) {
        if(response.userToken == null){
            clientContext.clean();
            view.displayText(response.error);
            return;
        }
        clientContext.setUsername(response.username);
        clientContext.setUserToken(response.userToken);
    }

    @Override
    public void handle(FindGameResponse response) {
        Game game = response.game;
        if (game != null) {
            game.initializeObservers();
            ClientContext.get().setCurrentGame(game);
            startPlaying();
        } else {
            view.displayText(response.error);
        }
    }

    @Override
    public void handle(GenericErrorResponse response) {
        view.displayText(response.error);
    }
}
