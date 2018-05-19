package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard6 extends ToolCard {

    public ToolCard6() {
        this.id = ToolCardConstants.TOOLCARD6_ID;
        this.name = ToolCardConstants.TOOL6_NAME;
        this.description = ToolCardConstants.TOOL6_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}
