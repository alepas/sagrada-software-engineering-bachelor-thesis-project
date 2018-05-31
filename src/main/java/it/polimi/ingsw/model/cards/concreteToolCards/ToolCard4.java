package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard4 extends ToolCard {

    public ToolCard4() {
        this.id = ToolCardConstants.TOOLCARD4_ID;
        this.name = ToolCardConstants.TOOL4_NAME;
        this.description = ToolCardConstants.TOOL4_DESCRIPTION;
        this.colorForDiceSingleUser=Color.YELLOW;
        this.allowPlaceDiceAfterCard=false;
        this.cardBlocksNextTurn=false;
        this.maxCancelStatus=3;
        this.cardOnlyInFirstMove=true;
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard4() ;
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return null;
    }

    @Override
    public MoveData placeDice(Dice dice, ClientDiceLocations startLocation, ClientDiceLocations finishLocation, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveData pickDice(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        return null;
    }

    @Override
    protected void cleanCard() {

    }

    @Override
    public MoveData getNextMove() {
        return null;
    }
}
