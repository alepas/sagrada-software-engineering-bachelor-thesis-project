package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.clientModel.Position;

import java.util.ArrayList;

public class ToolCardExtractedDicesModified implements Notification {
    public final String username;
    public final ArrayList<ClientDice> newExtractedDices;

    public ToolCardExtractedDicesModified(String username, ArrayList<ClientDice> newExtractedDices) {
        this.username = username;
        this.newExtractedDices = newExtractedDices;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
