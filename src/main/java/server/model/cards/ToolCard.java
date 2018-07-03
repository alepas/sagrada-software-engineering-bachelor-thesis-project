package server.model.cards;

import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.RoundTrack;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.Notification;

import java.util.ArrayList;

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


    public abstract ToolCard getToolCardCopy();

    public int getCurrentStatus() {
        return currentStatus;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean hasBeenUsed() {
        return used;
    }

    public abstract MoveData setCard(PlayerInGame player) throws CannotUseToolCardException;

    public abstract MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException;

    public abstract MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException;

    public abstract MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException;


    public ClientToolCard getClientToolcard() {
        return new ClientToolCard(id, name, description, used);
    }

    public abstract MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException;

    public abstract MoveData cancelAction(boolean all) throws CannotCancelActionException;


    protected abstract void cleanCard();

    public abstract MoveData getNextMove();


    protected void updateClientExtractedDices() {
        tempClientExtractedDices.clear();
        for (Dice dice : cardExtractedDices) {
            System.out.println(dice);
            tempClientExtractedDices.add(dice.getClientDice());
        }
    }

    protected void updateClientWPC() {
        tempClientWpc = cardWpc.getClientWpc();
    }

    protected void updateClientRoundTrack() {
        tempClientRoundTrack = cardRoundTrack.getClientRoundTrack();
    }

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


    protected MoveData cancelStatusZero() throws CannotCancelActionException {
        if (singlePlayerGame) {
            cleanCard();
            return new MoveData(true, true);
        }
        throw new CannotCancelActionException(username, id, 2);
    }



    protected MoveData pickDiceInitializeSingleUserToolCard(int diceId, NextAction nextAction, ClientDiceLocations initial, ClientDiceLocations finish) throws CannotPickDiceException {
        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        if (tempDice.getDiceColor() != colorForDiceSingleUser)
            throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
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
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (dicesOnWpc)
            if (player.getWPC().getNumOfDices() == 0)
                throw new CannotUseToolCardException(id, 5);
        if (dicesOnRoundTrack)
            if (player.getUpdatedRoundTrack().getNumberOfDices() == 0)
                throw new CannotUseToolCardException(id, 6);
        if (turn !=0){
            if (player.getTurnForRound() != turn){
                if (turn==1){
                    throw new CannotUseToolCardException(id, 7);
                }
                else throw new CannotUseToolCardException(id, 8);
            }
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

    protected MoveData defaultNextMoveStatusZero(){
        if (singlePlayerGame)
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, tempClientExtractedDices);
        else return null;
    }

    protected void copyExtractedDicesToCard(){
        cardExtractedDices.clear();
        cardExtractedDices.addAll(currentGame.getExtractedDices());
    }

    protected void copyExtractedDicesToGame(){
        currentGame.updateExtractedDices(cardExtractedDices);
    }

    protected void copyWpcToCard(){
        cardWpc=currentPlayer.getWPC().copyWpc();
    }

    protected void copyWpcToGame(){
       currentPlayer.updateWpc(cardWpc);
    }

    protected void copyRoundTrackToCard(){
        cardRoundTrack=currentGame.getRoundTrack().getCopy();
    }

    protected void copyRoundTrackToGame(){
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

    public void setCardExtractedDices(ArrayList<Dice> extractedDices){ cardExtractedDices = extractedDices;}

}
