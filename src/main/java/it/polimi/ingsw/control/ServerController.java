package it.polimi.ingsw.control;

import it.polimi.ingsw.control.network.commands.responses.*;
import it.polimi.ingsw.control.network.socket.SocketClientHandler;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.RoundTrack;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;
import it.polimi.ingsw.model.wpc.WPC;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class ServerController {
    // pieces of the model
    private final DatabaseUsers databaseUsers;
    private final DatabaseGames databaseGames;

    public ServerController(SocketClientHandler clientHandler) {
        this.databaseUsers = DatabaseUsers.getInstance();
        this.databaseGames = DatabaseGames.getInstance();
    }

    public void displayText(String text){
        System.out.println(">>> " + text);
    }

    public Response createUser(String username, String password, Socket socket) throws CannotRegisterUserException {
        String userToken = databaseUsers.registerUser(username, password, socket);
        displayText("Creato l'utente: " + username);
        return new CreateUserResponse(username, userToken, null);
    }

    public Response login(String username, String password, Socket socket) throws CannotLoginUserException {
        String userToken = databaseUsers.login(username, password, socket);
        displayText("Login avvenuto: " + username);
        return new LoginResponse(username, userToken, null);
    }

    public Response findGame(String userToken, int numPlayers, Observer observer) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException {
        String username = databaseUsers.getUsernameByToken(userToken);
        Game game = databaseGames.findGameForUser(username, numPlayers);

        game.addObserver(observer);

        return new FindGameResponse(game.getID(), game.numActualPlayers(), game.getNumPlayers(), null);
    }

    public Response pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Game game = player.getGame();
        game.setPlayerWpc(player, wpcID);

        return new PickWpcResponse(null);
    }

    public Response passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        databaseUsers.getPlayerInGameFromToken(userToken).endTurn();
        return new PassTurnResponse(null);
    }





    //----------------------------------------------- Toolcards --------------------------------------------------


    public Response setToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.setToolCard(cardId);
    }

    public Response pickDiceForToolCard(String userToken, int diceId, ClientDiceLocations where) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.pickDiceforToolCard(diceId,where);
    }

    public Response pickPositionForToolCard(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos=new Position(position.getRow(),position.getColumn());
        return player.pickPositionForToolCard(pos);
    }

    public Response pickColorForToolCard(String userToken, ClientColor color) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickColorException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Color colorTemp=Color.getColorFromClientColor(color);
        return player.pickColorForToolCard(colorTemp);
    }

    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return player.pickNumberForToolCard(number);
    }

    public Response pickDice(String userToken, int diceId) throws CannotPickDiceException, CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        player.pickDice(diceId);
        return new PickDiceResponse(null);
    }

    public Response pickPosition(String userToken, ClientPosition position) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPickPositionException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos=new Position(position.getRow(),position.getColumn());
        player.pickPosition(pos);
        return new PickPositionResponse(null);
    }

    public Response getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<Dice> dices= player.getUpdatedExtractedDices();
        ArrayList<ClientDice> clientDices=new ArrayList<>();
        for (Dice dice :dices){
            clientDices.add(dice.getClientDice());
        }
        return new UpdatedExtractedDicesResponse(clientDices);
    }

    public Response getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<PublicObjectiveCard> pocs= player.getUpdatedPOCs();
        ArrayList<ClientPoc> clientPocs=new ArrayList<>();
        for (PublicObjectiveCard poc :pocs){
            clientPocs.add(poc.getClientPoc());
        }
        return new UpdatedPOCsResponse(clientPocs);
    }

    public Response getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        RoundTrack roundTrack= player.getUpdatedRoundTrack();
        return new UpdatedRoundTrackResponse(roundTrack.getClientRoundTrack());
    }

    public Response getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<ToolCard> toolCards= player.getUpdatedToolCards();
        ArrayList<ClientToolCard> clientToolCards=new ArrayList<>();
        for (ToolCard card :toolCards){
            clientToolCards.add(card.getClientToolcard());
        }
        return new UpdatedToolCardsResponse(clientToolCards);
    }

    public Response getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        WPC wpc= player.getUpdatedWpc(username);
        return new UpdatedWPCResponse(username, wpc.getClientWpc());
    }

    public Response getUpdatedGame(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        Game tempGame=currentPlayer.getGame();
        HashMap<String,ClientWpc> wpcHashMap=new HashMap<>();
        for (PlayerInGame player: tempGame.getPlayers()){
            wpcHashMap.put(player.getUser(),player.getWPC().getClientWpc());
        }
        ArrayList<ToolCard> toolCards= currentPlayer.getUpdatedToolCards();
        ArrayList<ClientToolCard> clientToolCards=new ArrayList<>();
        for (ToolCard card :toolCards){
            clientToolCards.add(card.getClientToolcard());
        }
        ArrayList<PublicObjectiveCard> pocs= currentPlayer.getUpdatedPOCs();
        ArrayList<ClientPoc> clientPocs=new ArrayList<>();
        for (PublicObjectiveCard poc :pocs){
            clientPocs.add(poc.getClientPoc());
        }
        ArrayList<Dice> dices= currentPlayer.getUpdatedExtractedDices();
        ArrayList<ClientDice> clientDices=new ArrayList<>();
        for (Dice dice :dices){
            clientDices.add(dice.getClientDice());
        }
        ArrayList<ClientColor> tempPrivateObjects=new ArrayList<>();
        for (Color col: currentPlayer.getPrivateObjs())
            tempPrivateObjects.add(Color.getClientColor(col));
        ClientColor[] clientColors= (ClientColor[])tempPrivateObjects.toArray();

        return new UpdatedGameResponse(tempGame.getID(),tempGame.numActualPlayers(),tempGame.getNumPlayers(),clientColors, wpcHashMap,clientToolCards,clientPocs,tempGame.getRoundTrack().getCurrentRound(),clientDices,currentPlayer.getTurnForRound(),currentPlayer.isActive(), currentPlayer.getFavours(),tempGame.getRoundTrack().getClientRoundTrack());
    }

    public Response stopToolCard (String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotStopToolCardException, NoToolCardInUseException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        return currentPlayer.stopToolCard();
    }

    public Response cancelAction (String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        return currentPlayer.cancelAction();
    }


}
