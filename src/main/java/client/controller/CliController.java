package client.controller;

import client.network.ClientInfo;
import client.network.NetworkClient;
import client.view.cli.CliView;
import shared.clientInfo.*;
import shared.exceptions.gameexceptions.CannotCreatePlayerException;
import shared.exceptions.gameexceptions.InvalidGameParametersException;
import shared.exceptions.gameexceptions.NotYourWpcException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import static client.constants.CliConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class CliController {
    // reference to networking layer
    private final NetworkClient client;

    // the view
    private final CliView view;

    // Pieces of the model
    private ClientInfo clientInfo;

    public CliController(NetworkClient client) {
        this.client = client;
        this.clientInfo = ClientInfo.getInstance();
        this.view = new CliView(this);
    }

    public boolean run(){
        return view.launch();
    }

    public void addModelObserver(Observer observer){
        clientInfo.addObserver(observer);
    }

    public void removeModelObserver(Observer observer){
        clientInfo.removeObserver(observer);
    }

    public String createUser(String username, String password){
        try {
            client.createUser(username, password);
        } catch (CannotRegisterUserException e){
            view.displayText(e.getMessage());
        }

        return clientInfo.getUsername();
    }

    public String login(String username, String password){
        try {
            client.login(username, password);
        } catch (CannotLoginUserException e) {
            view.displayText(e.getMessage());
        }
        return clientInfo.getUsername();
    }

    public int findGame(int numPlayers){
        //Restituisce:
        // -1 se non ha trovato alcuna partita
        // 0  se ha trovato una partita ma mancano dei giocatori
        // 1  se ha trovato una partita e sono presenti tutti i giocatori
        try {
            client.findGame(clientInfo.getUserToken(), numPlayers,0);
        } catch (InvalidGameParametersException e) {
            view.displayText(e.getMessage());
        } catch (CannotFindUserInDBException e) {
            //TODO
            e.printStackTrace();
        } catch (CannotCreatePlayerException e) {
            view.displayText(e.getMessage());
            //TODO
        }

        String gameID = clientInfo.getGameID();
        if (gameID != null) {
            view.displayText(ENTER_GAME + gameID);
            view.displayText(PLAYER_CONNECTED + clientInfo.getGameActualPlayers() +
                    OF + clientInfo.getGameNumPlayers() + NEEDED);
            if (clientInfo.getGameActualPlayers() == clientInfo.getGameNumPlayers()) return 1;
            return 0;
        } else {
            return -1;
        }
    }

    public boolean pickWpc(String wpcID){
        try {
            client.pickWpc(clientInfo.getUserToken(), wpcID);
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
            client.passTurn(clientInfo.getUserToken());
            return true;
        } catch (PlayerNotAuthorizedException e) {
            view.displayText(e.getMessage());
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
            return client.placeDice(clientInfo.getUserToken(), id, position);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickPositionException | CannotPickDiceException | PlayerNotAuthorizedException | CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction useToolcard(String id){
        try {
            return client.useToolCard(clientInfo.getUserToken(), id);
        } catch (CannotFindPlayerInDatabaseException e){
            e.printStackTrace();
        } catch (PlayerNotAuthorizedException|CannotUseToolCardException |CannotPerformThisMoveException e){
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction pickDiceForToolCard(int id){
        try {
            return client.pickDiceForToolCard(clientInfo.getUserToken(), id);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (CannotPickDiceException|PlayerNotAuthorizedException|
                NoToolCardInUseException |CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public ClientUser getUserStat() {
        try {
            client.getUserStat(clientInfo.getUserToken());
            return clientInfo.getUser();
        } catch (CannotFindUserInDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NextAction placeDiceForToolCard(int id, Position pos){
        try {
            return client.placeDiceForToolCard(clientInfo.getUserToken(), id, pos);
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
            return client.pickNumberForToolCard(clientInfo.getUserToken(), num);
        } catch (CannotFindPlayerInDatabaseException e) {
            e.printStackTrace();
        } catch (PlayerNotAuthorizedException|NoToolCardInUseException|CannotPickNumberException
                |CannotPerformThisMoveException e) {
            view.displayText(e.getMessage());
        }
        return null;
    }

    public NextAction checkIfInGame(){
        try {
            return client.getUpdatedGame(clientInfo.getUserToken());
        } catch (CannotFindPlayerInDatabaseException e){
            return null;
        }
    }

    public NextAction interruptToolcard(ToolCardInterruptValues value){
        try {
            return client.interruptToolCard(clientInfo.getUserToken(), value);
        } catch (PlayerNotAuthorizedException | CannotInterruptToolCardException | CannotFindPlayerInDatabaseException | NoToolCardInUseException e){
            view.displayText(e.getMessage());
            return null;
        }
    }

    public NextAction cancelToolcardAction(){
        try {
            return client.cancelAction(clientInfo.getUserToken());
        } catch (CannotCancelActionException | PlayerNotAuthorizedException | CannotFindPlayerInDatabaseException e) {
            view.displayText(e.getMessage());
            return null;
        }
    }


    //---------------------------------- Request to cli model ----------------------------------

    public boolean areWpcsArrived(){
        return clientInfo.areAllWpcsArrived();
    }

    public boolean allPlayersChooseWpc(){
        return clientInfo.allPlayersChooseWpc();
    }

    public boolean isGameStarted(){
        return clientInfo.isGameStarted();
    }

    public boolean areAllPlayersInGame(){
        return clientInfo.areAllPlayersInGame();
    }

    public boolean arePrivateObjectivesArrived(){
        return clientInfo.arePrivateObjectivesArrived();
    }

    public boolean areToolcardsArrived(){
        return clientInfo.areToolcardsArrived();
    }

    public boolean arePocsArrived(){
        return clientInfo.arePocsArrived();
    }

    public String getUser(){
        return clientInfo.getUsername();
    }

    public ArrayList<ClientToolCard> getToolcards(){
        return clientInfo.getGameToolCards();
    }

    public ArrayList<ClientPoc> getPOC(){
        return clientInfo.getGamePublicObjectiveCards();
    }

    public int getCurrentRound(){
        return clientInfo.getCurrentRound();
    }

    public int getCurrentTurn(){
        return clientInfo.getCurrentTurn();
    }

    public ArrayList<ClientDice> getExtractedDices(){
        return clientInfo.getExtractedDices();
    }

    public boolean isInGame(){
        return clientInfo.getGameID() != null;
    }

    public boolean isActive(){
        return clientInfo.isActive();
    }

    public ClientWpc getMyWpc(){
        return clientInfo.getMyWpc();
    }

    public int getUserFavour(String username){
        return clientInfo.getFavoursByUsername().get(username);
    }

    public int getUserFavour(){
        return clientInfo.getFavoursByUsername().get(clientInfo.getUsername());
    }

    public HashMap<String, Integer> getFavoursByUsername(){
        return clientInfo.getFavoursByUsername();
    }

    public ClientColor[] getPrivateObjectives(){ return clientInfo.getPrivateObjectives(); }

    public ToolCardClientNextActionInfo getToolcardNextActionInfo(){
        return clientInfo.getToolCardClientNextActionInfo();
    }

    public ArrayList<ClientDice> getRoundtrackDices(){
        ArrayList<ClientDice> dices = new ArrayList<>();
        ClientDice[][] arrays = clientInfo.getRoundTrack().getAllDices();

        for(ClientDice[] array : arrays) {
            for (ClientDice dice :  array) {
                if (dice != null) dices.add(dice);
            }
        }

        return dices;
    }

    public void exitGame(){
        clientInfo.exitGame();
    }

    public ClientGame getGame(){
        return clientInfo.getGame();
    }

    public int getTimeToCompleteTask(){
        return clientInfo.getTimeLeftToCompleteTask();
    }

    public HashMap<String, ArrayList<ClientWpc>> getWpcsProposedByUsername(){
        return clientInfo.getWpcsProposedByUsername();
    }
}
