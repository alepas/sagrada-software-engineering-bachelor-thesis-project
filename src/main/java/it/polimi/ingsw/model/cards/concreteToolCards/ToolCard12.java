package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard12 extends ToolCard {

    public ToolCard12() {
        this.id = ToolCardConstants.TOOLCARD12_ID;
        this.name = ToolCardConstants.TOOL12_NAME;
        this.description = ToolCardConstants.TOOL12_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}
