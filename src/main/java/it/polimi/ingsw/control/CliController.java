package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.view.cli.CliView;

import java.util.ArrayList;
import java.util.HashMap;

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
        this.clientModel.addObserver(this.view);
    }

    public void run(){
        view.launch();
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
        //Restituisce:
        // -1 se non ha trovato alcuna partita
        // 0  se ha trovato una partita ma mancano dei giocatori
        // 1  se ha trovato una partita e sono presenti tutti i giocatori
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
            return true;
        } catch (NotYourWpcException e) {
            view.displayText(e.getMessage());
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }

    //Restituisce vero se è la richiesta è andata a buon fine
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

    public NextAction placeDice(int id, Position position) {
        try {
            return client.placeDice(clientModel.getUserToken(), id, position);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickPositionException e) {
            view.displayText("Impossibile posizionare il dado: posizione non valida");
        } catch (CannotPickDiceException|PlayerNotAuthorizedException|CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction useToolcard(String id){
        try {
            return client.useToolCard(clientModel.getUserToken(), id);
        } catch (CannotFindPlayerInDatabaseException e){
            e.printStackTrace();
        } catch (PlayerNotAuthorizedException|CannotUseToolCardException|CannotPerformThisMoveException e){
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction pickDiceForToolCard(int id){
        try {
            return client.pickDiceForToolCard(clientModel.getUserToken(), id);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickDiceException|PlayerNotAuthorizedException|
                NoToolCardInUseException|CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public ClientUser getUserStat() {
        try {
            client.getUserStat(clientModel.getUserToken());
            return clientModel.getUser();
        } catch (CannotFindUserInDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NextAction placeDiceForToolCard(int id, Position pos){
        try {
            return client.placeDiceForToolCard(clientModel.getUserToken(), id, pos);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickPositionException|PlayerNotAuthorizedException
                |NoToolCardInUseException|CannotPerformThisMoveException|CannotPickDiceException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction pickNumberForToolcard(int num){
        try {
            return client.pickNumberForToolCard(clientModel.getUserToken(), num);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (PlayerNotAuthorizedException|NoToolCardInUseException|CannotPickNumberException
                |CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }



    //---------------------------------- Request to cli model ----------------------------------

    public boolean areWpcsArrived(){
        return clientModel.areAllWpcsArrived();
    }

    public boolean allPlayersChooseWpc(){
        return clientModel.allPlayersChooseWpc();
    }

    public boolean isGameStarted(){
        return clientModel.isGameStarted();
    }

    public boolean areAllPlayersInGame(){
        return clientModel.areAllPlayersInGame();
    }

    public boolean arePrivateObjectivesArrived(){
        return clientModel.arePrivateObjectivesArrived();
    }

    public boolean areToolcardsArrived(){
        return clientModel.areToolcardsArrived();
    }

    public boolean arePocsArrived(){
        return clientModel.arePocsArrived();
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

    public int getUserFavour(String username){
        return clientModel.getFavoursByUsername().get(username);
    }

    public int getUserFavour(){
        return clientModel.getFavoursByUsername().get(clientModel.getUsername());
    }

    public HashMap<String, Integer> getFavoursByUsername(){
        return clientModel.getFavoursByUsername();
    }

    public ClientColor[] getPrivateObjectives(){ return clientModel.getPrivateObjectives(); }

    public ToolCardClientNextActionInfo getToolcardNextActionInfo(){
        return clientModel.getToolCardClientNextActionInfo();
    }

    public ArrayList<ClientDice> getRoundtrackDices(){
        ArrayList<ClientDice> dices = new ArrayList<>();
        ClientDice[][] arrays = clientModel.getRoundTrack().getAllDices();

        for(ClientDice[] array : arrays) {
            for (ClientDice dice :  array) {
                if (dice != null) dices.add(dice);
            }
        }

        return dices;
    }
}
