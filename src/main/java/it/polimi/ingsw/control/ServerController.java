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
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Wpc;

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
        Game game = databaseUsers.findNewGame(userToken, numPlayers, observer);
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
        Position pos = new Position(position.getRow(),position.getColumn());
        return convertMoveDataToPlaceDiceResponse(player.placeDice(id, pos));
    }


    public Response cancelAction(String userToken) throws CannotCancelActionException, PlayerNotAuthorizedException, CannotFindPlayerInDatabaseException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.cancelAction());
    }

    public Response getUserStat(String userToken) throws CannotFindUserInDBException {
        ClientUser user = databaseUsers.getClientUserByToken(userToken);
        return new GetUserStatResponse(user, null);
    }

    public void disconnectUser(String userToken) throws CannotFindPlayerInDatabaseException {
        try {
            System.out.println(databaseUsers.getUsernameByToken(userToken) + " si è disconesso");
        } catch (CannotFindUserInDBException e) {
            e.printStackTrace();
        }
        databaseUsers.getPlayerInGameFromToken(userToken).disconnect();
    }



    //----------------------------------------------- Toolcards --------------------------------------------------


    public Response setToolCard(String userToken, String cardId) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotUseToolCardException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.setToolCard(cardId));
    }

    public Response pickDiceForToolCard(String userToken, int diceId) throws CannotFindPlayerInDatabaseException, CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickDiceforToolCard(diceId));
    }

    public Response placeDiceForToolCard(String userToken, int diceId, Position position) throws CannotFindPlayerInDatabaseException, CannotPickPositionException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException, CannotPickDiceException {
        Position pos = null;
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        if(position != null) pos=new Position(position.getRow(),position.getColumn());
        return convertMoveDataToToolCardResponse(player.placeDiceForToolCard(diceId, pos));
    }

    public Response pickNumberForToolCard(String userToken, int number) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        PlayerInGame player=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(player.pickNumberForToolCard(number));
    }

    public Response interuptToolCard(String userToken, ToolCardInteruptValues value) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException, CannotInteruptToolCardException, NoToolCardInUseException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToToolCardResponse(currentPlayer.interuptToolCard(value));
    }





    //---------------------------update client model------------------------

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
        Wpc wpc= player.getUpdatedWpc(username);
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
        ClientColor []clientColors= new ClientColor[tempPrivateObjects.size()];
        tempPrivateObjects.toArray(clientColors);
        MoveData nextActionMove=currentPlayer.getNextMove();
        ToolCardClientNextActionInfo toolCardClientNextActionInfo=new ToolCardClientNextActionInfo(nextActionMove.wherePickNewDice,nextActionMove.wherePutNewDice,
                nextActionMove.numbersToChoose,nextActionMove.diceChosen, nextActionMove.diceChosenLocation, nextActionMove.messageForStop, nextActionMove.bothYesAndNo, nextActionMove.showBackButton);
        return new UpdatedGameResponse(tempGame.getID(),tempGame.numActualPlayers(),tempGame.getNumPlayers(),clientColors,wpcHashMap,clientToolCards,clientPocs,
                tempGame.getRoundTrack().getCurrentRound(),clientDices,tempGame.getCurrentTurn(),currentPlayer.isActive(),currentPlayer.getFavours(),
                tempGame.getRoundTrack().getClientRoundTrack(),nextActionMove.nextAction,toolCardClientNextActionInfo);
    }


    public Response getNextMove(String userToken) throws CannotFindPlayerInDatabaseException, PlayerNotAuthorizedException {
        PlayerInGame currentPlayer=databaseUsers.getPlayerInGameFromToken(userToken);
        return convertMoveDataToNextMoveResponse(currentPlayer.getNextMove());
    }


    public Response findAlreadyStartedGame(String userToken, Observer observer) throws CannotFindGameForUserInDatabaseException {
        Game game = databaseUsers.findAlreadyStartedGame(userToken, observer);
        if (game != null) {
            try {
                return getUpdatedGame(userToken);
            } catch (CannotFindPlayerInDatabaseException e) {
                throw new CannotFindGameForUserInDatabaseException();
            }

        }
        throw new CannotFindGameForUserInDatabaseException();
    }

    //--------private

    private ToolCardResponse convertMoveDataToToolCardResponse(MoveData moveData){
        return new ToolCardResponse(moveData.nextAction,moveData.wherePickNewDice,moveData.wherePutNewDice,moveData.numbersToChoose,moveData.wpc,
                moveData.extractedDices,moveData.roundTrack,moveData.diceChosen,moveData.diceChosenLocation,moveData.exception, moveData.messageForStop, moveData.bothYesAndNo,moveData.showBackButton);
    }


    private NextMoveResponse convertMoveDataToNextMoveResponse(MoveData moveData){
        return new NextMoveResponse(moveData.nextAction,moveData.wherePickNewDice,moveData.wherePutNewDice,moveData.numbersToChoose,moveData.wpc,
                moveData.extractedDices,moveData.roundTrack,moveData.diceChosen,moveData.exception);
    }

    private PlaceDiceResponse convertMoveDataToPlaceDiceResponse(MoveData moveData){
        return new PlaceDiceResponse(moveData.nextAction,moveData.wpc,moveData.extractedDices,moveData.roundTrack,moveData.exception);
    }


}
