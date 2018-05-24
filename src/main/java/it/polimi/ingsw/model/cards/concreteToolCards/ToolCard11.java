package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.responses.ToolCardResponse;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
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
    public ToolCard getToolCardCopy() {
        return new ToolCard11() ;
    }
    @Override
    public ToolCardResponse use(PlayerInGame player) {
        return new ToolCardResponse(null);
    }

    @Override
    public ToolCardResponse use(PlayerInGame player, Position pos) {
        return new ToolCardResponse(null);
    }

    @Override
    public ToolCardResponse use(PlayerInGame player, Dice dice) {
        return new ToolCardResponse(null);
    }

    @Override
    public ToolCardResponse use(PlayerInGame player, Color color) {
        return new ToolCardResponse(null);
    }

    @Override
    public ToolCardResponse use(PlayerInGame player, int number) {
        return new ToolCardResponse(null);
    }
}
