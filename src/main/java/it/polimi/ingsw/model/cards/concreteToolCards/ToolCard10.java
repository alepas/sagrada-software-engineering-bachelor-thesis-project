package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard10 extends ToolCard {

    public ToolCard10() {
        this.id = ToolCardConstants.TOOLCARD10_ID;
        this.name = ToolCardConstants.TOOL10_NAME;
        this.description = ToolCardConstants.TOOL10_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}