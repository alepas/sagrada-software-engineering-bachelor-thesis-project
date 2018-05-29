package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.responses.MoveResponse;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public class ToolCard3 extends ToolCard {

    public ToolCard3() {
        this.id = ToolCardConstants.TOOLCARD3_ID;
        this.name = ToolCardConstants.TOOL3_NAME;
        this.description = ToolCardConstants.TOOL3_DESCRIPTION;
        this.colorForDiceSingleUser=Color.RED;
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
        return new ToolCard3() ;
    }


    @Override
    public MoveResponse setCard(PlayerInGame player) throws CannotUseToolCardException {
        return null;
    }

    @Override
    public MoveResponse use(Position pos) throws CannotPickPositionException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveResponse use(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveResponse use(Color color) throws CannotPickColorException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveResponse use(int number) throws CannotPickNumberException, CannotPerformThisMoveException {
        return null;
    }

    @Override
    public MoveResponse cancelAction() throws CannotCancelActionException {
        return null;
    }

    @Override
    protected void cleanCard() {

    }

    @Override
    public MoveResponse getNextMove() {
        return null;
    }
}
