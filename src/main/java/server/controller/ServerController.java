package server.controller;

import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.MultiplayerGame;
import server.model.game.RoundTrack;
import server.model.users.DatabaseUsers;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import server.network.ClientHandler;
import shared.clientInfo.*;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidGameParametersException;
import shared.exceptions.gameExceptions.NotYourWpcException;
import shared.exceptions.gameExceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.responses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class ServerController {
    private static ServerController instance;
    // pieces of the model
    private final DatabaseUsers databaseUsers;
    private HashMap<String, ClientHandler> clientByUser;


    /**
     * Constructor of this
     */
    private ServerController() {
        this.databaseUsers = DatabaseUsers.getInstance();
        clientByUser = new HashMap<>();
    }

    /**
     * if the instance of this is null it calls its constructor if not it return the instance.
     */
    public static ServerController getInstance() {
        if (instance != null)
            return instance;
        instance = new ServerController();
        return instance;
    }


    /**
     * @param text is the text what will be display by calling this massage
     */
    public void displayText(String text) {
        System.out.println(">>> " + text);
    }


    /**
     * Sets the clientHandler equals to the element related to the given username in the HashMap; if it is null it
     * removes the coonection related to the client if not is adds the couple of information to the HashMap.
     *
     * @param username is the username of the player which wants to create a new connection
     * @param handler is the handler related to the player
     */
    public void addClientConnection(String username, ClientHandler handler) {
        ClientHandler client = clientByUser.get(username);
        if (client != null)
            client.removeConnection();
        clientByUser.put(username, handler);
    }

    /**
     * Gets a token for the player by calling the registerUser() method, adds the client connection and return a new
     * createUserResponse with parameters equals to the one of the new player.
     *
     * @param username is the new username that the player would like to add in the DB
     * @param password is the new password related to the new account that the player would like to create
     * @param handler is the Handler related to the username
     * @return the Response related to the creation of a new user in the db
     * @throws CannotRegisterUserException if it is not possible to register the user in the db
     */
    public Response createUser(String username, String password, ClientHandler handler) throws CannotRegisterUserException {
        String userToken = databaseUsers.registerUser(username, password);
        displayText("Creato l'utente: " + username);
        addClientConnection(username, handler);
        return new CreateUserResponse(username, userToken, null);

    }

    /**
     * Gets a token for the player by calling the login() method,  adds the clint connection and creates a new login
     * response.
     *
     * @param username is the username of a player that wants to login
     * @param password is the password of the account related to the given username
     * @param handler is the Handler related to the username
     * @return the Response related to the login of the user
     * @throws CannotLoginUserException if it is not possible to login the player
     */
    public Response login(String username, String password, ClientHandler handler) throws CannotLoginUserException {
        String userToken = databaseUsers.login(username, password);
        displayText("Login avvenuto: " + username);
        addClientConnection(username, handler);
        return new LoginResponse(username, userToken, null);
    }

    /**
     * calls the findNeGame() method, adds the observer and return a new findGameResponse
     *
     * @param userToken is the player token
     * @param numPlayers is the number of players that the current player would like to play with
     * @param levelOfDifficulty is a single player parameter, it is related to how many toolcard the player would
     *                          like to use
     * @param observer is the observer of this
     * @return the response related to the findGame method
     * @throws InvalidGameParametersException if the number is out of bound
     * @throws CannotFindUserInDBException if the player hasn't been found in the db
     * @throws CannotCreatePlayerException if  it is not possible to create the player
     */
    public Response findGame(String userToken, int numPlayers, int levelOfDifficulty, Observer observer) throws InvalidGameParametersException, CannotFindUserInDBException, CannotCreatePlayerException {
        Game game = databaseUsers.findNewGame(userToken, numPlayers, levelOfDifficulty);
        game.addObserver(observer);
        return new FindGameResponse(game.getID(), game.numActualPlayers(), game.getNumPlayers(), null);
    }

    /**
     * Finds the playerInGame by the token, calls the setWpc() method, return the response related to the pickWpc()
     *
     * @param userToken is the player token
     * @param wpcID is the id of the chosen schema
     * @return the response related to the pickWpc method
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws NotYourWpcException if the id is not one of the assigned id
     */
    public Response pickWpc(String userToken, String wpcID) throws CannotFindPlayerInDatabaseException, NotYourWpcException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Game game = player.getGame();
        game.setPlayerWpc(player, wpcID);

        return new PickWpcResponse(null);
    }

    /**
     * Gets the player in game and return a passturn response
     *
     * @param userToken is the player token
     * @return the response related to the pass turn action
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been fund in the db
     * @throws PlayerNotAuthorizedException if the player tries to do soething that can't do because isn't the active one
     * @throws CannotPerformThisMoveException if the player tries to do an action that can't be done for a few
     * different reasons
     */
    public Response passTurn(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        databaseUsers.getPlayerInGameFromToken(userToken).endTurn();
        return new PassTurnResponse(null);
    }

    /**
     * @param userToken is the player token
     * @param id is the id of the chosen dice
     * @param position is the position where the player would like to add the dice
     * @return the response related to the dice place
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been fund in the db
     * @throws CannotPickPositionException
     * @throws CannotPickDiceException if the d
     * @throws PlayerNotAuthorizedException if the player tries to do soething that can't do because isn't the active one
     * @throws CannotPerformThisMoveException  if the player tries to do an action that can't be done for a few
     * different reasons
     */
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
        ArrayList<Dice> dices = new ArrayList<>(player.getUpdatedExtractedDices());
        ArrayList<ClientDice> clientDices = new ArrayList<>();
        for (Dice dice : dices) {
            clientDices.add(dice.getClientDice());
        }
        return new UpdatedExtractedDicesResponse(clientDices);
    }

    public Response getUpdatedPOCs(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<PublicObjectiveCard> pocs = new ArrayList<>(player.getUpdatedPOCs());
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
        ArrayList<ToolCard> toolCards = new ArrayList<>(player.getUpdatedToolCards());
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


    public Response getNextMove(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToNextMoveResponse(currentPlayer.getNextMove());
    }

    public void removeSessionFromDatabase(String userToken) {
        databaseUsers.removeSession(userToken);
    }

    public void removeClient(String username, ClientHandler handler) {
        ClientHandler temp = clientByUser.get(username);
        if (temp.getToken().equals(handler.getToken()))
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
