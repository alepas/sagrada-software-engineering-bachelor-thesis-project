package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.control.network.commands.notifications.Notification;
import it.polimi.ingsw.model.clientModel.ClientToolCard;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public abstract class ToolCard implements Cloneable{
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
    protected boolean stoppable;
    protected boolean moveCancellable;
    protected int maxCancelStatus;
    protected PlayerInGame currentPlayer=null;
    protected Game currentGame=null;
    protected boolean singlePlayerGame=false;
    protected String username=null;
    protected ArrayList<Notification> movesNotifications;


    public abstract ToolCard getToolCardCopy();

    public int getCurrentStatus() {
        return currentStatus;
    }

    public String getID(){ return id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMoveCancellable() {
        return moveCancellable;
    }

    public Boolean hasBeenUsed() { return used; }

    public abstract MoveData setCard(PlayerInGame player) throws CannotUseToolCardException;
    public abstract MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException;
    public abstract MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException;
    public abstract MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException;


    public ClientToolCard getClientToolcard(){
        return new ClientToolCard(id, name, description, used);
    }

    public MoveData stopToolCard() throws CannotStopToolCardException {
        if (stoppable) {
            cleanCard();
            return new MoveData(true);
        } else throw new CannotStopToolCardException(username, id);

    }

    public abstract MoveData cancelAction() throws CannotCancelActionException;

    protected abstract void cleanCard();

    public abstract MoveData getNextMove();


}
