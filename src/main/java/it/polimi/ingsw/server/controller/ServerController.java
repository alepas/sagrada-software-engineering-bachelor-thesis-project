package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.server.model.cards.ToolCard;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiplayerGame;
import it.polimi.ingsw.server.model.game.RoundTrack;
import it.polimi.ingsw.server.model.users.DatabaseUsers;
import it.polimi.ingsw.server.model.users.MoveData;
import it.polimi.ingsw.server.model.users.PlayerInGame;
import it.polimi.ingsw.server.model.wpc.Wpc;
import it.polimi.ingsw.server.network.ClientHandler;
import it.polimi.ingsw.shared.clientInfo.*;
import it.polimi.ingsw.shared.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.shared.exceptions.gameExceptions.InvalidNumOfPlayersException;
import it.polimi.ingsw.shared.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.shared.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.shared.network.commands.responses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class ServerController {
    private static ServerController instance;
    // pieces of the model
    private final DatabaseUsers databaseUsers;
    private HashMap<String, ClientHandler> clientByUser;


    private ServerController() {
        this.databaseUsers = DatabaseUsers.getInstance();
        clientByUser = new HashMap<>();
    }

    public static ServerController getInstance() {
        if (instance != null)
            return instance;
        instance = new ServerController();
        return instance;
    }


    public void displayText(String text) {
        System.out.println(">>> " + text);
    }


    public void addClientConnection(String username, ClientHandler handler) {
        ClientHandler client = clientByUser.get(username);
        if (client != null)
            client.removeConnection();
        clientByUser.put(username, handler);
    }

    public Response createUser(String username, String password, ClientHandler handler) throws CannotRegisterUserException {
        String userToken = databaseUsers.registerUser(username, password);
        displayText("Creato l'utente: " + username);
        addClientConnection(username, handler);
        return new CreateUserResponse(username, userToken, null);

    }

    public Response login(String username, String password, ClientHandler handler) throws CannotLoginUserException {
        String userToken = databaseUsers.login(username, password);
        displayText("Login avvenuto: " + username);
        addClientConnection(username, handler);
        return new LoginResponse(username, userToken, null);
    }

    public Response findGame(String userToken, int numPlayers, int levelOfDifficulty, Observer observer) throws InvalidNumOfPlayersException, CannotFindUserInDBException, CannotCreatePlayerException {
        Game game = databaseUsers.findNewGame(userToken, numPlayers, levelOfDifficulty, observer);
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

    public Response placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos = new Position(position.getRow(), position.getColumn());
        return convertMoveDataToPlaceDiceResponse(player.placeDice(id, pos));
    }


    public Response cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.cancelAction());
    }

    public Response getUserStat(String userToken) throws CannotFindUserInDBException {
        ClientUser user = databaseUsers.getClientUserByToken(userToken);
        return new GetUserStatResponse(user, null);
    }


    //----------------------------------------------- Toolcards --------------------------------------------------


    public Response setToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.setToolCard(cardId));
    }

    public Response pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickDiceforToolCard(diceId));
    }

    public Response placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        Position pos = null;
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        if (position != null) pos = new Position(position.getRow(), position.getColumn());
        return convertMoveDataToToolCardResponse(player.placeDiceForToolCard(diceId, pos));
    }

    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickNumberForToolCard(number));
    }

    public Response interuptToolCard(String userToken, ToolCardInteruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.interuptToolCard(value));
    }


    //---------------------------update client model------------------------

    public Response getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<Dice> dices = player.getUpdatedExtractedDices();
        ArrayList<ClientDice> clientDices = new ArrayList<>();
        for (Dice dice : dices) {
            clientDices.add(dice.getClientDice());
        }
        return new UpdatedExtractedDicesResponse(clientDices);
    }

    public Response getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<PublicObjectiveCard> pocs = player.getUpdatedPOCs();
        ArrayList<ClientPoc> clientPocs = new ArrayList<>();
        for (PublicObjectiveCard poc : pocs) {
            clientPocs.add(poc.getClientPoc());
        }
        return new UpdatedPOCsResponse(clientPocs);
    }

    public Response getUpdatedRoundTrack(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        RoundTrack roundTrack = player.getUpdatedRoundTrack();
        return new UpdatedRoundTrackResponse(roundTrack.getClientRoundTrack());
    }

    public Response getUpdatedToolCards(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<ToolCard> toolCards = player.getUpdatedToolCards();
        ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();
        for (ToolCard card : toolCards) {
            clientToolCards.add(card.getClientToolcard());
        }
        return new UpdatedToolCardsResponse(clientToolCards);
    }

    public Response getUpdatedWPC(String userToken, String username) throws CannotFindPlayerInDatabaseException, UserNotInThisGameException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Wpc wpc = player.getUpdatedWpc(username);
        return new UpdatedWPCResponse(username, wpc.getClientWpc());
    }

    public Response getUpdatedGame(String userToken, Observer observer) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.findOldPlayer(userToken);
        Game game = player.getGame();
        Integer timeLeft = null;
        if (game.getNumPlayers() > 1) timeLeft = ((MultiplayerGame) game).getCurrentTaskTimeLeft();
        game.addObserver(observer);
        ClientGame clientGame = game.getClientGame();

        Color[] playerPrivateObjs = player.getPrivateObjs();
        ClientColor[] clientPrivateObjs = new ClientColor[playerPrivateObjs.length];
        for (int i = 0; i < playerPrivateObjs.length; i++) {
            clientPrivateObjs[i] = Color.getClientColor(playerPrivateObjs[i]);
        }

        MoveData nextActionMove = player.getNextMove();
        ToolCardClientNextActionInfo toolCardClientNextActionInfo = new ToolCardClientNextActionInfo(nextActionMove.wherePickNewDice, nextActionMove.wherePutNewDice,
                nextActionMove.numbersToChoose, nextActionMove.diceChosen, nextActionMove.diceChosenLocation, nextActionMove.messageForStop, nextActionMove.bothYesAndNo, nextActionMove.showBackButton);

        return new UpdatedGameResponse(clientGame, clientPrivateObjs, player.isActive(), timeLeft, nextActionMove.nextAction, toolCardClientNextActionInfo);
    }


    public Response getNextMove(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToNextMoveResponse(currentPlayer.getNextMove());
    }

    public void removeSessionFromDatabase(String userToken) {
        databaseUsers.removeClient(userToken);
    }

    public void removeClient(String username, ClientHandler handler) {
        ClientHandler temp = clientByUser.get(username);
        if (temp.getToken() == handler.getToken())
            clientByUser.remove(username);
    }

    public void deleteObserverFromGame(String user, Observer observer) throws CannotFindPlayerInDatabaseException {
        databaseUsers.getPlayerInGameFromUsername(user).getGame().deleteObserver(observer);
    }


    //--------private

    private ToolCardResponse convertMoveDataToToolCardResponse(MoveData moveData) {
        return new ToolCardResponse(moveData.nextAction, moveData.wherePickNewDice, moveData.wherePutNewDice, moveData.numbersToChoose, moveData.wpc,
                moveData.extractedDices, moveData.roundTrack, moveData.diceChosen, moveData.diceChosenLocation, moveData.exception, moveData.messageForStop, moveData.bothYesAndNo, moveData.showBackButton);
    }


    private NextMoveResponse convertMoveDataToNextMoveResponse(MoveData moveData) {
        return new NextMoveResponse(moveData.nextAction, moveData.wherePickNewDice, moveData.wherePutNewDice, moveData.numbersToChoose, moveData.wpc,
                moveData.extractedDices, moveData.roundTrack, moveData.diceChosen, moveData.exception);
    }

    private PlaceDiceResponse convertMoveDataToPlaceDiceResponse(MoveData moveData) {
        return new PlaceDiceResponse(moveData.nextAction, moveData.wpc, moveData.extractedDices, moveData.roundTrack, moveData.exception);
    }


}
