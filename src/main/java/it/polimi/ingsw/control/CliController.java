package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.wpc.Wpc;
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

    public boolean passTurn(){
        try {
            client.passTurn(clientModel.getUserToken());
            return true;
        } catch (PlayerNotAuthorizedException e) {
            view.displayText("Non puoi eseguire questo comando poichè non è il turno");
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
            //TODO
        } catch (CannotPerformThisMoveException e) {
           view.displayText(e.getMessage());
        }
        return false;
    }

    public void placeDice(int id, int col, int row) {
        try {
            client.placeDice(clientModel.getUserToken(), id, new Position(row, col));
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickPositionException e) {
            e.printStackTrace();
        } catch (CannotPickDiceException e) {
            e.printStackTrace();
        } catch (PlayerNotAuthorizedException e) {
            e.printStackTrace();
        } catch (CannotPerformThisMoveException e) {
            e.printStackTrace();
        }
    }



    //---------------------------------- Request to cli model ----------------------------------

    public boolean areAllWpcsReceived(){
        return clientModel.getWpcByUsername().size() == clientModel.getGameNumPlayers();
    }

    public String getUser(){
        return clientModel.getUsername();
    }

    public ArrayList<ClientToolCard> getToolcards(){
        return clientModel.getGameToolCards();
    }

    public ArrayList<ClientPoc> getPOC(){
        return clientModel.getGamePublicObjectiveCards();
    }

    public Wpc getWpcByID(String id){
        return clientModel.getWpcByID(id);
    }

    public int getCurrentRound(){
        return clientModel.getCurrentRound();
    }

    public int getCurrentTurn(){
        return clientModel.getCurrentTurn();
    }

    public ArrayList<ClientDice> getExtractedDices(){
        return clientModel.getExtractedDices();
    }

    public boolean isInGame(){
        return clientModel.getGameID() != null;
    }

    public boolean isActive(){
        return clientModel.isActive();
    }

    public ClientWpc getMyWpc(){
        return clientModel.getMyWpc();
    }

    public int getFavour(){
        return clientModel.getFavour();
    }
}
