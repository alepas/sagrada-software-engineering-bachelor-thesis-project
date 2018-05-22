package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public class ToolCard11 extends ToolCard {

    public ToolCard11() {
        this.id = ToolCardConstants.TOOLCARD11_ID;
        this.name = ToolCardConstants.TOOL11_NAME;
        this.description = ToolCardConstants.TOOL11_DESCRIPTION;
        this.currentPlayer=null;
        this.currentStatus=0;
    }

    @Override
    public ToolCard11 getToolCardCopy(){
        return new ToolCard11();
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
