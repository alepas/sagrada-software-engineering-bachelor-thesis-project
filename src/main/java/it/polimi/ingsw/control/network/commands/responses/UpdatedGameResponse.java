package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdatedGameResponse implements Response {
    public final ClientGame game;
    public final ClientColor[] privateObjectives;
    public final boolean active;
    public final Exception exception;
    public final NextAction nextAction;
    public final ToolCardClientNextActionInfo nextActionInfo;

    private UpdatedGameResponse(ClientGame game, boolean active, ClientColor[] privateObjectives, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo, Exception exception) {
        this.game = game;
        this.active = active;
        this.privateObjectives = privateObjectives;
        this.nextAction = nextAction;
        this.nextActionInfo = nextActionInfo;
        this.exception = exception;
    }

    public UpdatedGameResponse(ClientGame game, ClientColor[] privateObjectives, boolean active, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo) {
        this(game, active, privateObjectives, nextAction, nextActionInfo, null);
    }

    public UpdatedGameResponse(Exception exception) {
        this(null, false, null, null, null, exception);
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}