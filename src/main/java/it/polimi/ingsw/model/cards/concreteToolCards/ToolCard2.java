package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard2 extends ToolCard {

    public ToolCard2() {
        this.id = ToolCardConstants.TOOLCARD2_ID;
        this.name = ToolCardConstants.TOOL2_NAME;
        this.description = ToolCardConstants.TOOL2_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}
