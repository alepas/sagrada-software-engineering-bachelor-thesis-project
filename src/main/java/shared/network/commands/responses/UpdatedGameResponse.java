package shared.network.commands.responses;

import shared.clientInfo.ClientColor;
import shared.clientInfo.ClientGame;
import shared.clientInfo.NextAction;
import shared.clientInfo.ToolCardClientNextActionInfo;

public class UpdatedGameResponse implements Response {
    public final ClientGame game;
    public final ClientColor[] privateObjectives;
    public final boolean active;
    public final Integer timeLeftToCompleteTask;
    public final NextAction nextAction;
    public final ToolCardClientNextActionInfo nextActionInfo;
    public final Exception exception;

    private UpdatedGameResponse(ClientGame game, boolean active, Integer timeLeftToCompleteTask, ClientColor[] privateObjectives, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo, Exception exception) {
        this.game = game;
        this.active = active;
        this.timeLeftToCompleteTask = timeLeftToCompleteTask;
        this.privateObjectives = privateObjectives;
        this.nextAction = nextAction;
        this.nextActionInfo = nextActionInfo;
        this.exception = exception;
    }

    public UpdatedGameResponse(ClientGame game, ClientColor[] privateObjectives, boolean active, Integer timeLeftToCompleteTask, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo) {
        this(game, active, timeLeftToCompleteTask, privateObjectives, nextAction, nextActionInfo, null);
    }

    public UpdatedGameResponse(Exception exception) {
        this(null, false, null, null, null, null, exception);
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}