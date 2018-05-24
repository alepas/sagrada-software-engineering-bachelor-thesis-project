package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.control.network.commands.responses.ToolCardResponse;
import it.polimi.ingsw.model.clientModel.ClientToolCard;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

public abstract class ToolCard implements Cloneable{
    protected PlayerInGame currentPlayer=null;
    protected String id;
    protected String name;
    protected String description;
    protected Boolean used;
    protected int currentStatus;


    public abstract ToolCard getToolCardCopy();


    public String getID(){ return id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isUsed() { return used; }

    public abstract ToolCardResponse use(PlayerInGame player) throws CannotUseToolCardException;
    public abstract ToolCardResponse use(PlayerInGame player, Position pos)throws CannotPickPositionException;
    public abstract ToolCardResponse use(PlayerInGame player, Dice dice) throws CannotPickDiceException;
    public abstract ToolCardResponse use(PlayerInGame player, Color color) throws CannotPickColorException;
    public abstract ToolCardResponse use(PlayerInGame player, int number)throws CannotPickNumberException;

    public Response cancel(PlayerInGame player) throws CannotCancelToolCardException {
        if (currentPlayer==player) {
            currentPlayer = null;
            currentStatus = 0;
        }
        else throw new CannotCancelToolCardException(id,0);
        return new ToolCardResponse(null);
    }

    public ClientToolCard getClientToolcard(){
        return new ClientToolCard(id, name, description, used);
    }

}
