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
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;
import it.polimi.ingsw.view.CliView;

import java.util.HashMap;

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
        client.startPlaying(this, clientContext.getCurrentGameID());
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

    public String findGame(int numPlayers) {
        Request request = null;
        request = new FindGameRequest(clientContext.getUserToken(), numPlayers);
        client.request(request).handle(this);
        return clientContext.getCurrentGameID();
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
        String gameID = response.gameID;
        if (gameID != null) {
            ClientContext.get().setCurrentGameID(gameID);
            view.displayText("Aggiunto alla partita: " + gameID);
            view.displayText("Giocatori in partita: " + response.actualPlayers +
                        " di " + response.numPlayers + " necessari.");
            startPlaying();
        } else {
            view.displayText(response.error);
        }
    }



    //    ------------------ Notification handling -------------------------

    @Override
    public void handle(GenericErrorResponse response) {
        view.displayText(response.error);
    }

    @Override
    public void handle(GameStartedNotification response) {
        view.displayText("La partita è iniziata");
    }

    @Override
    public void handle(PlayersChangedNotification response) {
        if (response.joined == true){
            view.displayText(response.username + " si è unito alla partita.\n" +
                    ">>> Giocatori in partita: " + response.actualPlayers + " di "
                    + response.numPlayers + " necessari.");
        } else {
            view.displayText(response.username + " è uscito dalla partita.\n" +
                    ">>> Giocatori in partita: " + response.actualPlayers + " di "
                    + response.numPlayers + " necessari.");
        }
    }

    @Override
    public void handle(PrivateObjExtractedNotification response) {
        HashMap<String, Color[]> colorByUser = response.colorsByUser;
        Color[] playerColors = colorByUser.get(ClientContext.get().getUsername());
        StringBuilder str = new StringBuilder();
        if (playerColors.length == 1) str.append("Il tuo private objective è: ");
        else str.append("I tui private objective sono: ");
        for (Color color : playerColors){
            str.append(color + "\t");
        }
        view.displayText(str.toString());
    }
}
