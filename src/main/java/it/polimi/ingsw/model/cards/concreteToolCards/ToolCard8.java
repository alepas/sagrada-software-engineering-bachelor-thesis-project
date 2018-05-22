package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public class ToolCard8 extends ToolCard {

    public ToolCard8() {
        this.id = ToolCardConstants.TOOLCARD8_ID;
        this.name = ToolCardConstants.TOOL8_NAME;
        this.description = ToolCardConstants.TOOL8_DESCRIPTION;
        this.currentPlayer=null;
        this.currentStatus=0;
    }

    @Override
    public ToolCard8 getToolCardCopy(){
        return new ToolCard8();
    }

    @Override
    public void use(PlayerInGame player) {

    }

    @Override
    public void use(PlayerInGame player, Position pos) {

    }

    @Override
    public void use(PlayerInGame player, Dice dice) {

    }
}