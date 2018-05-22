package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public class ToolCard6 extends ToolCard {

    public ToolCard6() {
        this.id = ToolCardConstants.TOOLCARD6_ID;
        this.name = ToolCardConstants.TOOL6_NAME;
        this.description = ToolCardConstants.TOOL6_DESCRIPTION;
        this.currentPlayer=null;
        this.currentStatus=0;
    }

    @Override
    public ToolCard6 getToolCardCopy(){
        return new ToolCard6();
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
