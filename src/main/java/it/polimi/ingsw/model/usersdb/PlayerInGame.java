package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.PlayerSkipTurnNotification;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.game.RoundTrack;
import it.polimi.ingsw.model.wpc.DiceAndPosition;
import it.polimi.ingsw.model.wpc.Wpc;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;
import java.util.Observer;


public class PlayerInGame {
    private String username;
    private DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active = false;
    private Wpc wpc;
    private int turnForRound;
    private ToolCard toolCardInUse = null;
    private int lastFavoursRemoved;
    private boolean toolCardUsedInTurn;
    private boolean placedDiceInTurn;
    private boolean allowPlaceDiceAfterCard;
    private ToolCard cardUsedBlockingTurn;
    private Observer observer;


    public PlayerInGame(String user, Game game) throws CannotAddPlayerInDatabaseException {
        this.game = game;
        db = DatabaseUsers.getInstance();
        username = user;
        wpc = null;
        if (game instanceof MultiplayerGame) {
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

    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public String getUser() {
        return username;
    }

    public boolean isActive() {
        return active;
    }


    public void setToolCardInUse(ToolCard toolCardInUse) {
        this.toolCardInUse = toolCardInUse;
    }

    public int getTurnForRound() {
        return turnForRound;
    }

    public int getWonGames() throws CannotFindUserInDBException {
        return db.getWonGamesFromUsername(username);
    }

    public int getLostGames() throws CannotFindUserInDBException {
        return db.getLostGamesFromUsername(username);
    }

    public int getAbandonedGames() throws CannotFindUserInDBException {
        return db.getAbandonedGamesFromUsername(username);
    }

    public int getRanking() throws CannotFindUserInDBException {
        return db.getRankingFromUsername(username);
    }


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

    public void incrementTurnsForRound() {
        if (turnForRound < 3) {
            turnForRound++;
        } else if (turnForRound == 3) {
            clearPlayerRound();
            turnForRound++;
        }
        if ((turnForRound == 2) && (cardUsedBlockingTurn != null)) {
            game.changeAndNotifyObservers(new PlayerSkipTurnNotification(username, cardUsedBlockingTurn.getID()));
            clearPlayerTurn();
            game.nextTurn();
        }

    }

    public Wpc getWPC() {
        return wpc;
    }

    public boolean setWPC(String id) {
        if (wpc == null) {
            WpcDB dbWpc = WpcDB.getInstance();
            wpc = dbWpc.getWpcByID(id).copyWpc();
            favours = wpc.getFavours();
            return true;
        } else return false;

    }


    public int getFavours() {

        return favours;
    }


    public Color[] getPrivateObjs() {
        return privateObjs;
    }

    public void setPrivateObjs(Color color, int index) {
        privateObjs[index] = color;
    }


    public synchronized void endTurn() throws PlayerNotAuthorizedException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null) {

            clearPlayerTurn();
            game.nextTurn();
        } else throw new CannotPerformThisMoveException(username, 1, true);
    }

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
            throw new CannotPickDiceException(username, dice.getDiceNumber(), dice.getDiceColor(), ClientDiceLocations.EXTRACTED, 0);
        if (wpc.addDiceWithAllRestrictions(dice, pos) == false)
            throw new CannotPickPositionException(username, pos);
        game.getExtractedDices().remove(dice);
        ArrayList<ClientDice> tempExtractedDices = getClientExtractedDices();
        ClientWpc clientWpc = wpc.getClientWpc();
        game.changeAndNotifyObservers(new DicePlacedNotification(username, dice.getClientDice(), pos, clientWpc, tempExtractedDices, null));
        MoveData tempResponse = new MoveData(true, clientWpc, tempExtractedDices, null);
        tempResponse.setNextAction(incrementActionInTurn(false));
        return tempResponse;

    }


    public synchronized MoveData cancelAction() throws CannotCancelActionException, PlayerNotAuthorizedException {
        MoveData temp;
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse != null) {
            ToolCard oldCard = toolCardInUse;
            temp = toolCardInUse.cancelAction();
            if (temp.canceledToolCard) {
                favours += lastFavoursRemoved;
                allowPlaceDiceAfterCard=true;
                cardUsedBlockingTurn=null;

                if (!placedDiceInTurn)
                    temp.setNextAction(NextAction.MENU_ALL);
                else temp.setNextAction(NextAction.MENU_ONLY_TOOLCARD);

            }
            return temp;
        } else throw new CannotCancelActionException(username, null, 0);

    }

    public synchronized MoveData stopToolCard() throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotStopToolCardException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException(username);
        MoveData tempResponse = toolCardInUse.stopToolCard();
        if (tempResponse.moveFinished == true) {
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
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
                    tempResponse = card.setCard(this);
                } else throw new CannotUseToolCardException(cardID, 1);
            } else {
                System.out.println("l'utente ha favours: " + favours);
                if (favours >= 1) {
                    favours = favours - 1;
                    lastFavoursRemoved = 1;
                    tempResponse = card.setCard(this);
                } else throw new CannotUseToolCardException(cardID, 1);
            }
        } else {
            tempResponse = card.setCard(this);
        }
        if (tempResponse.moveFinished) {
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
        return tempResponse;
    }


    public synchronized MoveData pickDiceforToolCard(int diceId) throws CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);

        if (toolCardInUse == null)
            throw new NoToolCardInUseException(username);
        MoveData tempResponse;

        tempResponse = toolCardInUse.pickDice(diceId);
        if (tempResponse.moveFinished) {
            incrementActionInTurn(true);
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
        return tempResponse;

    }

    public synchronized MoveData placeDiceForToolCard(int diceId, Position pos) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickPositionException, CannotPerformThisMoveException, CannotPickDiceException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException(username);
        MoveData tempResponse;
        tempResponse = toolCardInUse.placeDice(diceId, pos);
        if (tempResponse.moveFinished) {
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
        return tempResponse;
    }


    public synchronized MoveData pickNumberForToolCard(int num) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException(username);
        MoveData tempResponse = toolCardInUse.pickNumber(num);
        if (tempResponse.moveFinished) {
            incrementActionInTurn(true);
            tempResponse.setNextAction(incrementActionInTurn(true));
        }
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

    public ArrayList<Dice> getUpdatedExtractedDices() {
        return game.getExtractedDices();
    }

    public RoundTrack getUpdatedRoundTrack() {
        return game.getRoundTrack();
    }

    public ArrayList<ToolCard> getUpdatedToolCards() {
        return game.getToolCards();
    }

    public ArrayList<PublicObjectiveCard> getUpdatedPOCs() {
        return game.getPublicObjectiveCards();
    }

    public Game getGame() {
        return game;
    }

    public synchronized void setActive() {
        this.active = true;
        incrementTurnsForRound();
    }


    public boolean isToolCardUsedInTurn() {
        return toolCardUsedInTurn;
    }

    public void setToolCardUsedInTurn(boolean toolCardUsedInTurn) {
        this.toolCardUsedInTurn = toolCardUsedInTurn;
    }

    public boolean isPlacedDiceInTurn() {
        return placedDiceInTurn;
    }

    public void setPlacedDiceInTurn(boolean placedDiceInTurn) {
        this.placedDiceInTurn = placedDiceInTurn;
    }

    public boolean isAllowPlaceDiceAfterCard() {
        return allowPlaceDiceAfterCard;
    }

    public void setAllowPlaceDiceAfterCard(boolean allowPlaceDiceAfterCard) {
        this.allowPlaceDiceAfterCard = allowPlaceDiceAfterCard;
    }

    public ToolCard getCardUsedBlockingTurn() {
        return cardUsedBlockingTurn;
    }

    public void setCardUsedBlockingTurn(ToolCard cardUsedBlockingTurn) {
        this.cardUsedBlockingTurn = cardUsedBlockingTurn;
    }

    public void setNotActive() {
        this.active = false;
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
                clearPlayerTurn();
                game.nextTurn();
                return NextAction.WAIT_FOR_TURN;
            } else return NextAction.MENU_ONLY_TOOLCARD;
        } else {
            if (toolCardUsedInTurn) {
                if (allowPlaceDiceAfterCard)
                    return NextAction.MENU_ONLY_PLACE_DICE;
                else {
                    clearPlayerTurn();
                    game.nextTurn();
                    return NextAction.WAIT_FOR_TURN;
                }
            }
        }
        return NextAction.MENU_ALL;
    }


    private void clearPlayerTurn() {
        toolCardUsedInTurn = false;
        placedDiceInTurn = false;
        lastFavoursRemoved = 0;
        allowPlaceDiceAfterCard = true;


    }

    private void clearPlayerRound() {
        turnForRound = 0;
        cardUsedBlockingTurn = null;
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
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 0);
        } else if (location == ClientDiceLocations.WPC) {
            tempResponse = wpc.getDiceAndPosition(diceId);
            if (tempResponse == null)
                throw new CannotPickDiceException(username, diceId, ClientDiceLocations.WPC, 0);
            else return tempResponse;

        } else if (location == ClientDiceLocations.ROUNDTRACK) {
            tempResponse = game.getRoundTrack().getDiceAndPosition(diceId);
            if (tempResponse == null)
                throw new CannotPickDiceException(username, diceId, ClientDiceLocations.ROUNDTRACK, 0);
            else return tempResponse;
        }
        throw new CannotPickDiceException(username, diceId, null, 0);
    }

}
