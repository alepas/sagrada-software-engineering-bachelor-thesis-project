package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;

import java.util.ArrayList;

public class ToolCardDiceChangedNotification implements Notification {
    public final String username;
    public final ClientDice oldDice;
    public final ClientDice newDice;
    public final ClientDiceLocations oldPosition;
    public final ClientDiceLocations newPosiition;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientWpc wpc;

    public ToolCardDiceChangedNotification(String username, ClientDice oldDice, ClientDice newDice, ClientDiceLocations oldPosition, ClientDiceLocations newPosiition, ArrayList<ClientDice> extractedDices) {
        this.username = username;
        this.oldDice = oldDice;
        this.newDice = newDice;
        this.oldPosition = oldPosition;
        this.newPosiition = newPosiition;
        this.extractedDices=extractedDices;
        this.roundTrack=null;
        this.wpc=null;

    }

    public ToolCardDiceChangedNotification(String username, ClientDice oldDice, ClientDice newDice, ClientDiceLocations oldPosition, ClientDiceLocations newPosiition, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack) {
        this.username = username;
        this.oldDice = oldDice;
        this.newDice = newDice;
        this.oldPosition = oldPosition;
        this.newPosiition = newPosiition;
        this.extractedDices=extractedDices;
        this.roundTrack=roundTrack;
        this.wpc=null;

    }
    public ToolCardDiceChangedNotification(String username, ClientDice oldDice, ClientDice newDice, ClientDiceLocations oldPosition, ClientDiceLocations newPosiition, ArrayList<ClientDice> extractedDices, ClientWpc wpc) {
        this.username = username;
        this.oldDice = oldDice;
        this.newDice = newDice;
        this.oldPosition = oldPosition;
        this.newPosiition = newPosiition;
        this.extractedDices=extractedDices;
        this.roundTrack=null;
        this.wpc=wpc;

    }
    public ToolCardDiceChangedNotification(String username, ClientDice oldDice, ClientDice newDice, ClientDiceLocations oldPosition, ClientDiceLocations newPosiition, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientWpc wpc) {
        this.username = username;
        this.oldDice = oldDice;
        this.newDice = newDice;
        this.oldPosition = oldPosition;
        this.newPosiition = newPosiition;
        this.extractedDices=extractedDices;
        this.roundTrack=roundTrack;
        this.wpc=wpc;

    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
