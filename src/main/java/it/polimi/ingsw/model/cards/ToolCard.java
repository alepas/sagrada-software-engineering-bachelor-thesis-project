package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.control.network.commands.responses.MoveResponse;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

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
    protected int maxCancelStatus;
    protected PlayerInGame currentPlayer=null;
    protected Game currentGame=null;
    protected boolean singlePlayerGame=false;
    protected String username=null;


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

    public Boolean isUsed() { return used; }

    public abstract MoveResponse setCard(PlayerInGame player) throws CannotUseToolCardException;
    public abstract MoveResponse use( Position pos) throws CannotPickPositionException, CannotPerformThisMoveException;
    public abstract MoveResponse use( Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException;
    public abstract MoveResponse use( Color color) throws CannotPickColorException, CannotPerformThisMoveException;
    public abstract MoveResponse use( int number) throws CannotPickNumberException, CannotPerformThisMoveException;


    public ClientToolCard getClientToolcard(){
        return new ClientToolCard(id, name, description, used);
    }

    public MoveResponse stopToolCard() throws CannotStopToolCardException {
        if (stoppable) {
            cleanCard();
            return new MoveResponse(null,ClientNextActions.MOVEFINISHED, true, ClientToolCardStatus.SETTEDCARD,false);
        } else throw new CannotStopToolCardException(username, id);

    }

    public abstract MoveResponse cancelAction() throws CannotCancelActionException;

    protected abstract void cleanCard();

    public abstract MoveResponse getNextMove();


}
