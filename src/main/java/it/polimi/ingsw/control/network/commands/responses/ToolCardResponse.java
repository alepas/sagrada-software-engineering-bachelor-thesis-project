package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;
import it.polimi.ingsw.model.clientModel.ClientToolCardModifiedThings;

import java.util.ArrayList;

public class ToolCardResponse implements Response {
    public final ClientToolCardModifiedThings modifiedElement;
    public final ClientToolCardModes nextAction;
    public final ArrayList<ClientDice> dices;
    public final ClientDice diceForPosition;
    public final ArrayList<ClientColor> colors;
    public final ArrayList<Integer> numbers;
    public final Exception exception;

    public ToolCardResponse(ClientToolCardModifiedThings modifiedElement, ClientToolCardModes nextAction, ArrayList<ClientDice> dices, ClientDice diceForPosition, ArrayList<ClientColor> colors, ArrayList<Integer> numbers, Exception exception) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.dices = dices;
        this.diceForPosition = diceForPosition;
        this.colors = colors;
        this.numbers = numbers;
        this.exception = exception;
    }

    public ToolCardResponse(Exception exception) {
        this.exception = exception;
        this.nextAction=null;
        this.diceForPosition=null;
        this.dices=null;
        this.modifiedElement=null;
        this.numbers=null;
        this.colors=null;
    }

    public ToolCardResponse(ClientToolCardModifiedThings modified, Exception exception) {
        this.exception = exception;
        this.nextAction=null;
        this.diceForPosition=null;
        this.dices=null;
        this.modifiedElement=modified;
        this.numbers=null;
        this.colors=null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}