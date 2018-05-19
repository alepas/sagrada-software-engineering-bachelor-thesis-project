package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.view.cli.CliView;

import java.util.ArrayList;

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
            view.displayText(e.getMessage());
        }

        return clientModel.getUsername();
    }

    public String login(String username, String password){
        try {
            client.login(username, password);
        } catch (CannotLoginUserException e) {
            view.displayText(e.getMessage());
        }
        return clientModel.getUsername();
    }

    public int findGame(int numPlayers){
        try {
            client.findGame(clientModel.getUserToken(), numPlayers);
        } catch (InvalidNumOfPlayersException e) {
            view.displayText(e.getMessage());
        } catch (CannotFindUserInDBException e) {
            //TODO
            e.printStackTrace();
        } catch (CannotCreatePlayerException e) {
            view.displayText(e.getMessage());
            //TODO
        }

        String gameID = clientModel.getGameID();
        if (gameID != null) {
            clientModel.setObserver(view);
            view.displayText("Entrato nella partita: " + gameID);
            view.displayText("Giocatori presenti: " + clientModel.getGameActualPlayers() +
                    " di " + clientModel.getGameNumPlayers() + " necessari.");
            if (clientModel.getGameActualPlayers() == clientModel.getGameNumPlayers()) return 1;
            return 0;
        } else {
            return -1;
        }
    }

    public boolean pickWpc(String wpcID){
        try {
            client.pickWpc(clientModel.getUserToken(), wpcID);
            view.displayText("Estratto wpc correttamente");
            return true;
        } catch (NotYourWpcException e) {
            view.displayText(e.getMessage());
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }




    //---------------------------------- Request to cli model ----------------------------------

    public boolean areAllWpcsReceived(){
        return clientModel.wpcByUsername.size() == clientModel.getGameNumPlayers();
    }

    public WPC getWpcByID(String id){
        return clientModel.getWpcByID(id);
    }

    public ArrayList<ToolCard> getToolcard(){
        return clientModel.getGameToolCards();
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards(){
        return clientModel.getGamePublicObjectiveCards();
    }
}
