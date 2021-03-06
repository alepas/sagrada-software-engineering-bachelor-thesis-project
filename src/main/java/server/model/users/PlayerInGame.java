package server.model.users;

import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.MultiplayerGame;
import server.model.game.RoundTrack;
import server.model.wpc.DiceAndPosition;
import server.model.wpc.Wpc;
import server.model.wpc.WpcDB;
import shared.clientinfo.*;
import shared.exceptions.gameexceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.DicePlacedNotification;
import shared.network.commands.notifications.PlayerDisconnectedNotification;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the player in a game. It keeps stored every game detail related to the player and the information
 * about the current round and turn. It contains all the methods that take care of all the possible actions for the player
 * in the turn.
 * The controller on server will call the playerInGame correseponding to the user and this object will provide the
 */
public class PlayerInGame {
    private String username;
    private DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active;
    private Wpc wpc;
    private int turnForRound;
    private ToolCard toolCardInUse;
    private int lastFavoursRemoved;
    private boolean toolCardUsedInTurn;
    private boolean placedDiceInTurn;
    private boolean allowPlaceDiceAfterCard;
    private ToolCard cardUsedBlockingTurn;
    private boolean disconnected = false;
    private Integer turnToSkip;


    /**
     * PlayerInGame constructor: creates a new PlayerInGame object setting the parameters to the default values.
     * Then it calls a method to store the created object in the users database.
     *
     * @param user is the username of the user who will become the player
     * @param game is the game object of the match that the player will be taking part
     */
    public PlayerInGame(String user, Game game) {
        this.game = game;
        db = DatabaseUsers.getInstance();
        username = user;
        wpc = null;
        if (!game.isSinglePlayerGame()) {
            privateObjs = new Color[GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME];
        } else {
            privateObjs = new Color[GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME];
        }
        favours = 0;
        active = false;
        db.addPlayerInGameToDB(this);
        placedDiceInTurn = false;
        allowPlaceDiceAfterCard = true;
        toolCardUsedInTurn = false;
        lastFavoursRemoved = 0;
        turnForRound = 0;
        toolCardInUse=null;
        cardUsedBlockingTurn=null;
    }

    //------------------------Set-Up methods and update playerInGame attributes methods--------------

    /**
     * gets an instance of the wpc and copy it.
     *
     * @param id is the id of the wpc
     */
    public void setWPC(String id) {
        WpcDB dbWpc = WpcDB.getInstance();
        wpc = dbWpc.getWpcByID(id).copyWpc();
        favours = wpc.getFavours();
    }

    public void updateWpc(Wpc tempWpc) {
        wpc=tempWpc;
    }

    public void setPrivateObjs(Color color, int index) {
        privateObjs[index] = color;
    }


    public void setAllowPlaceDiceAfterCard(boolean allowPlaceDiceAfterCard) {
        this.allowPlaceDiceAfterCard = allowPlaceDiceAfterCard;
    }


    public void setCardUsedBlockingTurn(ToolCard cardUsedBlockingTurn) {
        this.cardUsedBlockingTurn = cardUsedBlockingTurn;
    }

    public void setToolCardInUse(ToolCard toolCardInUse) {
        this.toolCardInUse = toolCardInUse;
    }


    //--------connection methods

    void reConnect() {
        this.disconnected = false;
    }




    //--------------turn methods

    public synchronized void setActive() {
        this.active = true;
    }

    public void setNotActive() {
        this.active = false;
    }


    public synchronized void endTurn() throws PlayerNotAuthorizedException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null) {
            game.endTurn(null);
        } else throw new CannotPerformThisMoveException(username, 1, true);
    }

    public synchronized void forceEndTurn(){
        MoveData temp=null;
        ToolCard oldToolCard=null;
        ClientToolCard oldClientToolCard=null;
        if (toolCardInUse == null) {
            game.endTurn(null);
        } else{
            try {
                oldToolCard=toolCardInUse;
                temp = toolCardInUse.cancelAction(true);
                oldClientToolCard=oldToolCard.getClientToolcard();
            } catch (CannotCancelActionException e) {
                e.printStackTrace();
            }
            if (temp==null)
                return;
            if (temp.canceledToolCard) {
                oldClientToolCard = null;
                toolCardInUse=null;
            }
            ClientEndTurnData endTurnData= new ClientEndTurnData(username,temp.wpc,oldClientToolCard,temp.extractedDices,temp.roundTrack);
            game.endTurn(endTurnData);
        }
    }



    //-----------------------Getters---------------------------

    public String getUser() {
        return username;
    }

    public int getTurnForRound() { return turnForRound; }


    public boolean isActive() {
        return active;
    }



    public int getFavours() {

        return favours;
    }

    public Wpc getWPC() {
        return wpc;
    }



    public boolean isPlacedDiceInTurn() {
        return placedDiceInTurn;
    }

    public boolean isAllowPlaceDiceAfterCard() {
        return allowPlaceDiceAfterCard;
    }



    public List<Dice> getUpdatedExtractedDices() {
        return game.getExtractedDices();
    }

    public RoundTrack getUpdatedRoundTrack() {
        return game.getRoundTrack();
    }

    public List<ToolCard> getUpdatedToolCards() {
        return game.getToolCards();
    }

    public List<PublicObjectiveCard> getUpdatedPOCs() {
        return game.getPublicObjectiveCards();
    }

    public Game getGame() {
        return game;
    }

    public ToolCard getCardUsedBlockingTurn() {
        return cardUsedBlockingTurn;
    }

    public Color[] getPrivateObjs() {
        return privateObjs;
    }



    //------------update user statistics on user database



    public void addPointsToRanking(int pointsToAdd) throws CannotUpdateStatsForUserException {
        db.addPointsRankingFromUsername(username, pointsToAdd);

    }

    public void addWonGame() throws CannotUpdateStatsForUserException {
        db.addWonGamesFromUsername(username);

    }

    public void addLostGame() throws CannotUpdateStatsForUserException {
        db.addLostGamesFromUsername(username);


    }

    public void addAbandonedGame() throws CannotUpdateStatsForUserException {
        db.addAbandonedGamesFromUsername(username);

    }













//-------game actions

    public synchronized MoveData getNextMove() {
        if (!active)
            return new MoveData(NextAction.WAIT_FOR_TURN);
        if (toolCardInUse == null) {
            if ((!placedDiceInTurn) && (!toolCardUsedInTurn))
                return new MoveData(NextAction.MENU_ALL);
            else if (toolCardUsedInTurn && placedDiceInTurn)
                return new MoveData(NextAction.MENU_ONLY_ENDTURN);
            else if (placedDiceInTurn)
                return new MoveData(NextAction.MENU_ONLY_TOOLCARD);
            else
                return new MoveData(NextAction.MENU_ONLY_PLACE_DICE);
        } else return toolCardInUse.getNextMove();
    }

    public synchronized MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPerformThisMoveException, PlayerNotAuthorizedException, CannotPickPositionException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username, 1, false);
        }
        if (placedDiceInTurn) {
            throw new CannotPerformThisMoveException(username, 2, false);
        }
        Dice dice = null;
        for (Dice tempDice : game.getExtractedDices()) {
            if (tempDice.getId() == diceId) {
                dice = tempDice;
                break;
            }
        }
        if (dice == null)
            throw new CannotPickDiceException(diceId, ClientDiceLocations.EXTRACTED, 0);
        if (!wpc.addDiceWithAllRestrictions(dice, pos))
            throw new CannotPickPositionException();
        game.getExtractedDices().remove(dice);
        ArrayList<ClientDice> tempExtractedDices = getClientExtractedDices();
        ClientWpc clientWpc = wpc.getClientWpc();
        game.changeAndNotifyObservers(new DicePlacedNotification(username, dice.getClientDice(), pos, clientWpc, tempExtractedDices, null));
        MoveData tempResponse = new MoveData(true, clientWpc, tempExtractedDices, null);
        tempResponse.setNextAction(incrementActionInTurn(false));
        return tempResponse;

    }





    public synchronized MoveData setToolCard(String cardID) throws CannotUseToolCardException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        MoveData tempResponse;
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username, 1, false);
        }
        if ((toolCardUsedInTurn))
            throw new CannotPerformThisMoveException(username, 3, false);

        boolean foundCard = false;
        ToolCard card = null;
        for (ToolCard tempCard : game.getToolCards()) {
            if (tempCard.getID().equals(cardID)) {
                foundCard = true;
                card = tempCard;
                break;
            }
        }
        if (!foundCard)
            throw new CannotUseToolCardException(cardID, 0);
        if (game instanceof MultiplayerGame) {

            if (card.hasBeenUsed()) {
                if (favours >= 2) {
                    favours = favours - 2;
                    lastFavoursRemoved = 2;
                } else throw new CannotUseToolCardException(cardID, 1);
            } else {
                if (favours >= 1) {
                    favours = favours - 1;
                    lastFavoursRemoved = 1;

                } else throw new CannotUseToolCardException(cardID, 1);
            }
            try {
                tempResponse = card.setCard(this);
            }catch (CannotUseToolCardException e){
                favours+=lastFavoursRemoved;
                throw e;
            }
        } else {
            tempResponse = card.setCard(this);
        }
        updateNextMoveAfterToolCard(tempResponse);
        return tempResponse;
    }


    public synchronized MoveData pickDiceforToolCard(int diceId) throws CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);

        if (toolCardInUse == null)
            throw new NoToolCardInUseException();
        MoveData tempResponse;

        tempResponse = toolCardInUse.pickDice(diceId);
        updateNextMoveAfterToolCard(tempResponse);
        return tempResponse;
    }

    public synchronized MoveData placeDiceForToolCard(int diceId, Position pos) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickPositionException, CannotPerformThisMoveException, CannotPickDiceException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException();
        MoveData tempResponse;
        tempResponse = toolCardInUse.placeDice(diceId, pos);
        updateNextMoveAfterToolCard(tempResponse);
        return tempResponse;
    }


    public synchronized MoveData pickNumberForToolCard(int num) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException();
        MoveData tempResponse = toolCardInUse.pickNumber(num);
        updateNextMoveAfterToolCard(tempResponse);
        return tempResponse;
    }

    public synchronized MoveData cancelAction() throws CannotCancelActionException, PlayerNotAuthorizedException {
        MoveData temp;
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse != null) {
            temp = toolCardInUse.cancelAction(false);
            if (temp==null)
                throw new CannotCancelActionException(username, null, 3);
            if (temp.canceledToolCard) {
                toolCardInUse=null;
                favours += lastFavoursRemoved;
                allowPlaceDiceAfterCard = true;
                cardUsedBlockingTurn = null;
                if (!placedDiceInTurn)
                    temp.setNextAction(NextAction.MENU_ALL);
                else temp.setNextAction(NextAction.MENU_ONLY_TOOLCARD);
            }
            return temp;
        } else throw new CannotCancelActionException(username, null, 0);

    }

    public synchronized MoveData interruptToolCard(ToolCardInterruptValues value) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotInterruptToolCardException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException();
        MoveData tempResponse = toolCardInUse.interruptToolCard(value);
        updateNextMoveAfterToolCard(tempResponse);
        return tempResponse;
    }





    public Wpc getUpdatedWpc(String username) throws UserNotInThisGameException {
        if (this.username.equals(username))
            return wpc;
        for (PlayerInGame player : game.getPlayers()) {
            if (player.getUser().equals(username))
                return player.wpc;
        }
        throw new UserNotInThisGameException(username, this.game);
    }



    private ArrayList<ClientDice> getClientExtractedDices() {
        ArrayList<ClientDice> tempExtractedDices = new ArrayList<>();
        for (Dice tempdice : getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
        return tempExtractedDices;

    }

    private NextAction incrementActionInTurn(boolean cardAction) {
        if (cardAction)
            toolCardUsedInTurn = true;
        else
            placedDiceInTurn = true;

        if (placedDiceInTurn) {
            if (toolCardUsedInTurn) {
                return NextAction.MENU_ONLY_ENDTURN;
            } else return NextAction.MENU_ONLY_TOOLCARD;
        } else {
            if (allowPlaceDiceAfterCard)
                return NextAction.MENU_ONLY_PLACE_DICE;
            else {
                return NextAction.MENU_ONLY_ENDTURN;
            }
        }
    }


    private void clearPlayerTurn() {
        toolCardUsedInTurn = false;
        placedDiceInTurn = false;
        lastFavoursRemoved = 0;
        allowPlaceDiceAfterCard = true;

    }

    public void clearPlayerRound() {
        cardUsedBlockingTurn = null;
        turnToSkip=null;
        clearPlayerTurn();
    }


    public DiceAndPosition dicePresentInLocation(int diceId, ClientDiceLocations location) throws CannotPickDiceException {
        DiceAndPosition tempResponse;
        if (location == ClientDiceLocations.EXTRACTED) {
            for (Dice tempDice : game.getExtractedDices()) {
                if (tempDice.getId() == diceId) {
                    return new DiceAndPosition(tempDice, null);
                }
            }
            throw new CannotPickDiceException(diceId, ClientDiceLocations.EXTRACTED, 0);
        } else if (location == ClientDiceLocations.WPC) {
            tempResponse = wpc.getDiceAndPosition(diceId);
            if (tempResponse == null)
                throw new CannotPickDiceException(diceId, ClientDiceLocations.WPC, 0);
            else return tempResponse;

        } else if (location == ClientDiceLocations.ROUNDTRACK) {
            tempResponse = game.getRoundTrack().getDiceAndPosition(diceId);
            if (tempResponse == null)
                throw new CannotPickDiceException(diceId, ClientDiceLocations.ROUNDTRACK, 0);
            else return tempResponse;
        }
        throw new CannotPickDiceException(diceId, null, 0);
    }


    private void updateNextMoveAfterToolCard(MoveData tempResponse){
        if (tempResponse.moveFinished) {
            game.removeToolCardIfSingleGame(toolCardInUse);
            toolCardInUse=null;
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
        else if (tempResponse.canceledToolCard){
            favours += lastFavoursRemoved;
            allowPlaceDiceAfterCard = true;
            cardUsedBlockingTurn = null;
            placedDiceInTurn=true;
            toolCardInUse=null;
            tempResponse.setNextAction(NextAction.MENU_ONLY_TOOLCARD);
        }
    }

    public void setTurnInRound(int i) {
        turnForRound=i;
        if (turnForRound==1)
            clearPlayerRound();
        if (turnForRound==2)
            clearPlayerTurn();
    }

    void disconnect() {
        try {
            disconnected = true;
            game.disconnectPlayer(username);
            if (active) forceEndTurn();
            game.changeAndNotifyObservers(new PlayerDisconnectedNotification(username));
        } catch (UserNotInThisGameException e){ }
    }

    public boolean isDisconnected() {
        return disconnected;
    }


    public Integer getTurnToSkip() {
        return turnToSkip;
    }

    public void setTurnToSkip(Integer turnToSkip) {
        this.turnToSkip = turnToSkip;
    }
}
