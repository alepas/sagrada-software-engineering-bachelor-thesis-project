package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public class ToolCard4 extends ToolCard {

    public ToolCard4() {
        this.id = ToolCardConstants.TOOLCARD4_ID;
        this.name = ToolCardConstants.TOOL4_NAME;
        this.description = ToolCardConstants.TOOL4_DESCRIPTION;
        this.currentPlayer=null;
        this.currentStatus=0;
    }

    @Override
    public ToolCard4 getToolCardCopy(){
        return new ToolCard4();
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
