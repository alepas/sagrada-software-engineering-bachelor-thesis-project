package server.model.cards;

import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.RoundTrack;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import shared.clientinfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.Notification;

import java.util.ArrayList;
import java.util.List;

public abstract class ToolCard implements Cloneable {
    protected String id;
    protected String name;
    protected String description;
    protected Color colorForDiceSingleUser;
    protected boolean allowPlaceDiceAfterCard;
    protected boolean cardBlocksNextTurn;
    protected boolean cardOnlyInFirstMove;
    protected Boolean used;
    protected Dice diceForSingleUser;
    protected int currentStatus;
    protected PlayerInGame currentPlayer = null;
    protected Game currentGame = null;
    protected boolean singlePlayerGame = false;
    protected String username = null;
    protected ArrayList<Notification> movesNotifications;
    protected ArrayList<Dice> cardExtractedDices;
    protected Wpc cardWpc;
    protected RoundTrack cardRoundTrack;
    protected ArrayList<ClientDice> tempClientExtractedDices;
    protected ClientWpc tempClientWpc;
    protected ClientRoundTrack tempClientRoundTrack;


    /**
     * @return the copy of the tool card
     */
    public abstract ToolCard getToolCardCopy();


    /**
     * @return the status of the used tool card
     */
    public int getCurrentStatus() {
        return currentStatus;
    }

    /**
     * @return the id of the used tool card
     */
    public String getID() {
        return id;
    }

    /**
     * @return the tool card name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the tool card description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return true if the tool card as been already used, false if not
     */
    public Boolean hasBeenUsed() {
        return used;
    }

    /**
     * @param player is the player in game that would like to use a tool card
     * @return the first action with all parameters that will tell the player what to do
     * @throws CannotUseToolCardException if the player can't use the tool card
     */
    public abstract MoveData setCard(PlayerInGame player) throws CannotUseToolCardException;

    /**
     * this method is abstract because it depends and varies from tool card to tool card.
     *
     * @param diceId is the id of the chosen dice
     * @param pos is the position where the player would like to add the chosen dice
     * @return the next action with all parameters related to the next status
     * @throws CannotPickDiceException  if the dice can't be taken
     * @throws CannotPickPositionException id pos = null
     * @throws CannotPerformThisMoveException if it is not possible to place the dice in the chosen position
     */
    public abstract MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException;

    /**
     * this method is abstract because it depends and varies from tool card to tool card.
     *
     * @param diceId is the id of the chosen dice
     * @return the next action with all parameters related to the next status
     * @throws CannotPickDiceException if the dice can't be taken
     * @throws CannotPerformThisMoveException if it is not possible to place the dice in the chosen position
     */
    public abstract MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException;

    /**
     * this method is abstract because it depends and varies from tool card to tool card: it is used in two tool card.
     *
     * @param number is the number chosen by the player
     * @return the next action with all parameters related to the next status
     * @throws CannotPickNumberException if the chosen number is out of bound
     * @throws CannotPerformThisMoveException if it is not possible to place the dice in the chosen position
     */
    public abstract MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException;


    /**
     * @return the client tool card
     */
    public ClientToolCard getClientToolcard() {
        return new ClientToolCard(id, name, description, Color.getClientColor(colorForDiceSingleUser), used);
    }

    /**
     * @param value can be YES, OK, NO
     * @return the next action with all parameters related to the next status
     * @throws CannotInterruptToolCardException if the status isn't correct and for that reason it is not possible to
     * interrupt the tool card
     */
    public abstract MoveData interruptToolCard(ToolCardInterruptValues value) throws CannotInterruptToolCardException;

    /**
     * @param all boolean
     * @return the next action with all parameters related to the next status
     * @throws CannotCancelActionException if it is not possible to cancel the action because of the wrong status
     */
    public abstract MoveData cancelAction(boolean all) throws CannotCancelActionException;


    /**
     * delete all changes done in the tool card during the usage
     */
    protected abstract void cleanCard();

    /**
     * @return the next action with all parameters related to the next status
     */
    public abstract MoveData getNextMove();


    /**
     * modifies the client dices array
     */
    protected void updateClientExtractedDices() {
        tempClientExtractedDices.clear();
        for (Dice dice : cardExtractedDices) tempClientExtractedDices.add(dice.getClientDice());
    }

    /**
     * modifies the client schema by taking the copy done dureing the usage of the toolcard
     */
    protected void updateClientWPC() {
        tempClientWpc = cardWpc.getClientWpc();
    }

    protected void updateClientRoundTrack() {
        tempClientRoundTrack = cardRoundTrack.getClientRoundTrack();
    }

    /**
     * This method is called if the status is equal to one and the player wants to cancel the action that has just
     * done. if the game is a multi player game the only thing that  must be done is to end the card and clean it by calling
     * the clean method; if the game is a single player game the dice picked to activate the toolcard is added to the
     * extracted dices and creates a new MoveData with as nextAction the possibility to select a dice to use the tool card.
     *
     * @return the previous action with all parameters related to the next status
     */
    protected MoveData cancelStatusOne() {
        if (!singlePlayerGame) {
            cleanCard();
            return new MoveData(true, true);
        }
        cardExtractedDices.add(diceForSingleUser);
        updateClientExtractedDices();
        diceForSingleUser = null;
        this.currentStatus = 0;
        return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempClientExtractedDices, null, null, null);
    }


    /**
     * cleans the card and return the end of toolcard.
     *
     * @return the moveData that ends the tool card
     * @throws CannotCancelActionException if the game is a multi player game, thats because the status 0 is related only
     * to the single player came
     */
    protected MoveData cancelStatusZero() throws CannotCancelActionException {
        if (singlePlayerGame) {
            cleanCard();
            return new MoveData(true, true);
        }
        throw new CannotCancelActionException(username, id, 2);
    }


    /**
     * checks if the dice color is equal to the color request by the tool card to be activated: if this is true it
     * removes the dice from the extracted and goes to the next action and status.
     *
     * @param diceId is the id of the chosen dice
     * @param nextAction is the action that the player has to do
     * @param initial is the initial location where the player has to pick the dice
     * @param finish is the final position where the player has to place the dice
     * @return the next action with all parameters related to the next status
     * @throws CannotPickDiceException if the color of the chosen dice isn't the color that must have the dice to active
     * the tool card
     */
    protected MoveData pickDiceInitializeSingleUserToolCard(int diceId, NextAction nextAction, ClientDiceLocations initial, ClientDiceLocations finish) throws CannotPickDiceException {
        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        if (tempDice.getDiceColor() != colorForDiceSingleUser)
            throw new CannotPickDiceException(tempDice.getDiceNumber(), Color.getClientColor(tempDice.getDiceColor()), ClientDiceLocations.EXTRACTED, 1);
        this.currentStatus = 1;
        this.diceForSingleUser = tempDice;
        cardExtractedDices.remove(this.diceForSingleUser);
        updateClientExtractedDices();
        return new MoveData(nextAction, initial, finish, null, tempClientExtractedDices, null, null, null);

    }


    protected MoveData setCardDefault(PlayerInGame player, boolean dicesOnWpc, boolean dicesOnRoundTrack, int turn, ClientDiceLocations from, ClientDiceLocations finish, NextAction nextAction) throws CannotUseToolCardException {
        setCardInitialStep(player, dicesOnWpc, dicesOnRoundTrack, turn);
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            return new MoveData(nextAction, from, finish);
        }
    }

    protected void setCardInitialStep(PlayerInGame player, boolean dicesOnWpc, boolean dicesOnRoundTrack, int turn) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 9);
        }
        if (cardOnlyInFirstMove && player.isPlacedDiceInTurn()) throw new CannotUseToolCardException(id, 2);
        if (dicesOnWpc && player.getWPC().getNumOfDices() == 0) throw new CannotUseToolCardException(id, 5);
        if (dicesOnRoundTrack && player.getUpdatedRoundTrack().getNumberOfDices() == 0)
            throw new CannotUseToolCardException(id, 6);
        if (turn != 0 && player.getTurnForRound() != turn) {
            if (turn == 1) {
                throw new CannotUseToolCardException(id, 7);
            } else throw new CannotUseToolCardException(id, 8);
        }
        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        copyExtractedDicesToCard();
        copyWpcToCard();
        copyRoundTrackToCard();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientExtractedDices();
    }

    /**
     * sets null everything
     */
    protected void defaultClean() {
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.currentGame = null;
        this.username = null;
        this.tempClientWpc = null;
        this.tempClientRoundTrack = null;
        this.singlePlayerGame = false;
        this.tempClientExtractedDices = new ArrayList<>();
        this.movesNotifications = new ArrayList<>();
        this.cardExtractedDices =new ArrayList<>();
        this.cardRoundTrack = null;
        this.cardWpc =null;
    }

    /**
     * @return the MoveData to chose the dice to activate the toolcard if the game is a single player one
     */
    protected MoveData defaultNextMoveStatusZero(){
        if (singlePlayerGame)
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, tempClientExtractedDices);
        else return null;
    }

    private void copyExtractedDicesToCard(){
        cardExtractedDices.clear();
        cardExtractedDices.addAll(currentGame.getExtractedDices());
    }

    private void copyExtractedDicesToGame(){
        currentGame.updateExtractedDices(cardExtractedDices);
    }

    private void copyWpcToCard(){
        cardWpc=currentPlayer.getWPC().copyWpc();
    }

    private void copyWpcToGame(){
       currentPlayer.updateWpc(cardWpc);
    }

    private void copyRoundTrackToCard(){
        cardRoundTrack=currentGame.getRoundTrack().getCopy();
    }

    private void copyRoundTrackToGame(){
        currentGame.updateRoundTrack(cardRoundTrack);
    }


    protected MoveData cancelCardFinalAction(){
        ClientWpc tempWpc = currentPlayer.getWPC().getClientWpc();
        ArrayList<ClientDice> tempExtracted = new ArrayList<>();
        tempClientExtractedDices.clear();
        for (Dice tempdice : currentGame.getExtractedDices())
            tempExtracted.add(tempdice.getClientDice());
        ClientRoundTrack tempRound = currentGame.getRoundTrack().getClientRoundTrack();
        cleanCard();
        return new MoveData(true, true, tempWpc, tempExtracted, tempRound, null, null, null);
    }

    protected void updateAndCopyToGameData(boolean extracted, boolean wpc, boolean roundtrack){
        if (extracted){
            copyExtractedDicesToGame();
            updateClientExtractedDices();
        }
        if (wpc){
            copyWpcToGame();
            updateClientWPC();
        }
        if (roundtrack) {
            copyRoundTrackToGame();
            updateClientRoundTrack();
        }
    }


    //----------------------------------- Used in testing -------------------------------------------------------

    public void setCurrentToolPlayer(PlayerInGame player){ currentPlayer = player; }

    public void setCurrentToolGame(Game game){ currentGame = game; }

    public void setCurrentToolStatus(int status){currentStatus = status;}

    public void setToolUser(String username){ this.username = username;}

    public void setSinglePlayerGame(boolean singlePlayer){ singlePlayerGame = singlePlayer;}

    public void setCardWpc (Wpc wpc){cardWpc = wpc;}

    public void setCardRoundTrack(RoundTrack roundTrack){cardRoundTrack = roundTrack;}

    public Wpc getCardWpc(){ return cardWpc;}

    public void setCardExtractedDices(List<Dice> extractedDices){ cardExtractedDices = new ArrayList<>(extractedDices);}

}
