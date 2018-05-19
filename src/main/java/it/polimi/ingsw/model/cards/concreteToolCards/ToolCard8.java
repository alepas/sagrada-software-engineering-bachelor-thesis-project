package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard8 extends ToolCard {

    public ToolCard8() {
        this.id = ToolCardConstants.TOOLCARD8_ID;
        this.name = ToolCardConstants.TOOL8_NAME;
        this.description = ToolCardConstants.TOOL8_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}