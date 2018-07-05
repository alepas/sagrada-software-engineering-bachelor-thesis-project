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
    private void displayText(String text) {
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
     *
     * @return the response related to the pickWpc method
     *
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
     * Gets the player in game and return a passTurn response
     *
     * @param userToken is the player token
     *
     * @return the response related to the pass turn action
     *
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
     * gets the player from his/her token and returns a place dice response.
     *
     * @param userToken is the player token
     * @param id is the id of the chosen dice
     * @param position is the position where the player would like to add the dice
     *
     * @return the response related to the dice place
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been fund in the db
     * @throws CannotPickPositionException if the position isn't correct
     * @throws CannotPickDiceException if the chosen dice is incorrect for some reason
     * @throws PlayerNotAuthorizedException if the player tries to do soething that can't do because isn't the active one
     * @throws CannotPerformThisMoveException  if the player tries to do an action that can't be done for a few
     * different reasons
     */
    public Response placeDice(String userToken, int id, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        Position pos = new Position(position.getRow(), position.getColumn());
        return convertMoveDataToPlaceDiceResponse(player.placeDice(id, pos));
    }


    /**
     * gets the player by his/her token returns the response after converting the player's MoveData
     * @param userToken is the token related to the player
     *
     * @return the response related to the dice toolcard
     *
     * @throws CannotCancelActionException if it not possible to cancel the toolcard action
     * @throws PlayerNotAuthorizedException if the player tries to do soething that can't do because isn't the active one
     * @throws CannotFindPlayerInDatabaseException  if the player hasn't been found in the db
     */
    public Response cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.cancelAction());
    }

    /**
     * Gets the user by the players token and returns a new response
     *
     * @param userToken is the player's token
     *
     * @return the getUserStatResponse
     *
     * @throws CannotFindUserInDBException if the player hasn't been found in the db
     */
    public Response getUserStat(String userToken) throws CannotFindUserInDBException {
        ClientUser user = databaseUsers.getClientUserByToken(userToken);
        return new GetUserStatResponse(user, null);
    }


    //----------------------------------------------- Toolcards --------------------------------------------------


    /**
     * @param userToken is the player's token
     * @param cardId is the toolcard's id
     *
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws PlayerNotAuthorizedException if the player tries to do soething that can't do because isn't the active one
     * @throws CannotUseToolCardException if there's a problem related to the use of a toolcard
     * @throws CannotPerformThisMoveException if the player tries to do an action that can't be done for a few
     * different reasons
     */
    public Response setToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.setToolCard(cardId));
    }

    /**
     * @param userToken is the player's token
     * @param diceId is the id of the chosen dice
     *
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws CannotPickDiceException if the chosen dice is incorrect for some reason
     * @throws PlayerNotAuthorizedException if the player tries to do something that can't do because isn't the active one
     * @throws NoToolCardInUseException if the player is not using the toolcard
     * @throws CannotPerformThisMoveException if the player tries to do an action that can't be done for a few
     *         different reasons
     */
    public Response pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickDiceforToolCard(diceId));
    }

    /**
     * if the chosen position is different from null returns a placeDiceForToolCard response
     *
     * @param userToken is the player's token
     * @param diceId is the chosen dice id
     * @param position is the position where the player would like to add the chosen dice
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws CannotPickPositionException if the position isn't correct
     * @throws PlayerNotAuthorizedException if the player tries to do something that can't do because isn't the active one
     * @throws NoToolCardInUseException if the player is not using the toolcard
     * @throws CannotPerformThisMoveException if the player tries to do an action that can't be done for a few
     *  different reasons
     * @throws CannotPickDiceException if the chosen dice is incorrect for some reason
     */
    public Response placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        Position pos = null;
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        if (position != null) pos = new Position(position.getRow(), position.getColumn());
        return convertMoveDataToToolCardResponse(player.placeDiceForToolCard(diceId, pos));
    }

    /**
     * @param userToken is the player token
     * @param number is the number chosen by the player
     *
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws PlayerNotAuthorizedException if the player tries to do something that can't do because isn't the active one
     * @throws NoToolCardInUseException if the player is not using the toolcard
     * @throws CannotPickNumberException if the chosen number is out of bound
     * @throws CannotPerformThisMoveException if the player tries to do an action that can't be done for a few
     * different reasons
     */
    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickNumberForToolCard(number));
    }

    /**
     * @param userToken is the player's token
     * @param value it can be OK, YES, NO
     *
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     * @throws PlayerNotAuthorizedException if the player tries to do something that can't do because isn't the active one
     * @throws CannotInterruptToolCardException if the value isn't correct
     * @throws NoToolCardInUseException if the player is not using the toolcard
     */
    public Response interruptToolCard(String userToken, ToolCardInterruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInterruptToolCardException, NoToolCardInUseException {
        PlayerInGame currentPlayer = databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.interruptToolCard(value));
    }


    //---------------------------update client model------------------------

    /**
     * Creates a new ArrayList composed by the extracted dices, adds the clientDices to an other arrayList and returns
     * a new Updated response.
     *
     * @param userToken is the player's token
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     */
    public Response getUpdatedExtractedDices(String userToken) throws CannotFindPlayerInDatabaseException {
        PlayerInGame player = databaseUsers.getPlayerInGameFromToken(userToken);
        ArrayList<Dice> dices = new ArrayList<>(player.getUpdatedExtractedDices());
        ArrayList<ClientDice> clientDices = new ArrayList<>();

        for (Dice dice : dices)
            clientDices.add(dice.getClientDice());

        return new UpdatedExtractedDicesResponse(clientDices);
    }

    /**
     * Creates a new ArrayList composed by the public objective cards, adds them to an other arrayList composed with the
     * client cards and returns a new Updated response.
     *
     * @param userToken is the player's token
     * @return a new response
     *
     * @throws CannotFindPlayerInDatabaseException if the player hasn't been found in the db
     */
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

    /**
     * @param moveData is the object containing all the information of the action done by the player
     * @return the ToolDiceResponse
     */
    private ToolCardResponse convertMoveDataToToolCardResponse(MoveData moveData) {
        return new ToolCardResponse(moveData.nextAction, moveData.wherePickNewDice, moveData.wherePutNewDice, moveData.numbersToChoose, moveData.wpc,
                moveData.extractedDices, moveData.roundTrack, moveData.diceChosen, moveData.diceChosenLocation, moveData.exception, moveData.messageForStop, moveData.bothYesAndNo, moveData.showBackButton);
    }


    private NextMoveResponse convertMoveDataToNextMoveResponse(MoveData moveData) {
        return new NextMoveResponse(moveData.nextAction, moveData.wherePickNewDice, moveData.wherePutNewDice, moveData.numbersToChoose, moveData.wpc,
                moveData.extractedDices, moveData.roundTrack, moveData.diceChosen, moveData.exception);
    }

    /**
     * @param moveData is the object containing all the information of the action done by the player
     * @return the placeDiceResponse
     */
    private PlaceDiceResponse convertMoveDataToPlaceDiceResponse(MoveData moveData) {
        return new PlaceDiceResponse(moveData.nextAction, moveData.wpc, moveData.extractedDices, moveData.roundTrack, moveData.exception);
    }


}
