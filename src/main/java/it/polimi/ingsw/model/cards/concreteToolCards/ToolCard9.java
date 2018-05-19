package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ToolCard9 extends ToolCard {

    public ToolCard9() {
        this.id = ToolCardConstants.TOOLCARD9_ID;
        this.name = ToolCardConstants.TOOL9_NAME;
        this.description = ToolCardConstants.TOOL9_DESCRIPTION;
    }

    @Override
    public void use(PlayerInGame player) {

    }
}
