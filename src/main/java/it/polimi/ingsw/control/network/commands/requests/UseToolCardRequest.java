package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;

public class UseToolCardRequest implements Request {
    public final String userToken;
    public ClientToolCardModes mode;
    public final String toolCardId;
    public final int diceId;
    public final ClientDiceLocations where;
    public final ClientPosition position;
    public final ClientColor color;
    public final int number;

    public UseToolCardRequest(String userToken, String toolCardId) {
        this.userToken = userToken;
        mode=ClientToolCardModes.SETCARD;
        this.toolCardId = toolCardId;
        this.diceId=0;
        this.where=null;
        this.position=null;
        this.color=null;
        this.number=0;
    }

    public UseToolCardRequest(String userToken, int diceId, ClientDiceLocations where) {
        this.userToken = userToken;
        mode=ClientToolCardModes.DICE;
        this.toolCardId = null;
        this.diceId=diceId;
        this.where=where;
        this.position=null;
        this.color=null;
        this.number=0;
    }

    public UseToolCardRequest(String userToken, ClientPosition position) {
        this.userToken = userToken;
        mode=ClientToolCardModes.POSITION;
        this.toolCardId = null;
        this.diceId=0;
        this.where=null;
        this.position=position;
        this.color=null;
        this.number=0;
    }

    public UseToolCardRequest(String userToken, ClientColor color) {
        this.userToken = userToken;
        mode=ClientToolCardModes.COLOR;
        this.toolCardId = null;
        this.diceId=0;
        this.where=null;
        this.position=null;
        this.color=color;
        this.number=0;
    }

    public UseToolCardRequest(String userToken, int number) {
        this.userToken = userToken;
        mode=ClientToolCardModes.NUMBER;
        this.toolCardId = null;
        this.diceId=0;
        this.where=null;
        this.position=null;
        this.color=null;
        this.number=number;
    }


    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
