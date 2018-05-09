package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;
import it.polimi.ingsw.view.cli.CliView;

public class CliController {
    // reference to networking layer
    private final NetworkClient client;

    // the view
    private final CliView view;

    // Pieces of the model
    private ClientModel clientModel;

    public CliController(NetworkClient client) {
        this.client = client;
        this.clientModel = ClientModel.getInstance();
        this.view = new CliView(this);
    }

    public void run(){
        do {
            if (view.logPhase()) view.login();
            else view.createUsername();
        } while (clientModel.getUserToken() == null);

        view.mainMenuPhase();
    }

    public String createUser(String username, String password){
        try {
            client.createUser(username, password);
        } catch (CannotRegisterUserException e){
            e.printStackTrace();
        }

        return clientModel.getUsername();
    }

    public String login(String username, String password){
        try {
            client.login(username, password);
        } catch (CannotLoginUserException e) {
            e.printStackTrace();
            //TODO
        }
        return clientModel.getUsername();
    }

    public String findGame(int numPlayers) {
        try {
            client.findGame(clientModel.getUserToken(), numPlayers);
        } catch (InvalidPlayersException e) {
            //TODO
            e.printStackTrace();
        } catch (NullTokenException e) {
            //TODO
            e.printStackTrace();
        } catch (CannotFindUserInDBException e) {
            //TODO
            e.printStackTrace();
        }

        String gameID = clientModel.getGameID();
        if (gameID != null) {
            clientModel.setObserver(view);
            view.displayText("Entrato nella partita: " + gameID);
            view.displayText("Giocatori presenti: " + clientModel.getGameActualPlayers() +
                    " di " + clientModel.getGameNumPlayers() + " necessari.");
        }
        return clientModel.getGameID();
    }


    //------------------------------- Notification Handler -------------------------------

}
