package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard3 extends ToolCard {

    public ToolCard3() {
        this.id = ToolCardConstants.TOOLCARD3_ID;
        this.name = ToolCardConstants.TOOL3_NAME;
        this.description = ToolCardConstants.TOOL3_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}
