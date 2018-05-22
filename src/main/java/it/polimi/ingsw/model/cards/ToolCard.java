package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.clientModel.ClientToolCard;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotCancelToolCardException;
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

    public abstract void use(PlayerInGame player);
    public abstract void use(PlayerInGame player, Position pos);
    public abstract void use(PlayerInGame player, Dice dice);

    public void cancel(PlayerInGame player) throws CannotCancelToolCardException {
        if (currentPlayer==player) {
            currentPlayer = null;
            currentStatus = 0;
        }
        else throw new CannotCancelToolCardException(id,0);
    }

    public ClientToolCard getClientToolcard(){
        return new ClientToolCard(id, name, description, used);
    }

}
