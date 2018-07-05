package shared.network.commands.responses;

import shared.clientinfo.ClientColor;
import shared.clientinfo.ClientGame;
import shared.clientinfo.NextAction;
import shared.clientinfo.ToolCardClientNextActionInfo;

public class UpdatedGameResponse implements Response {
    public final ClientGame game;
    public final ClientColor[] privateObjectives;
    public final boolean active;
    public final Integer timeLeftToCompleteTask;
    public final NextAction nextAction;
    public final ToolCardClientNextActionInfo nextActionInfo;
    public final Exception exception;

    /**
     * @param game is the played game
     * @param active true if the player is active
     * @param timeLeftToCompleteTask is how much time the player has to complete the task
     * @param privateObjectives is the array with all possible private colors of the player
     * @param nextAction is the action that the player must do
     * @param nextActionInfo is the next action hat the player has to do
     * @param exception is the exception that could be thrown
     */
    private UpdatedGameResponse(ClientGame game, boolean active, Integer timeLeftToCompleteTask, ClientColor[] privateObjectives, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo, Exception exception) {
        this.game = game;
        this.active = active;
        this.timeLeftToCompleteTask = timeLeftToCompleteTask;
        this.privateObjectives = privateObjectives;
        this.nextAction = nextAction;
        this.nextActionInfo = nextActionInfo;
        this.exception = exception;
    }

    /**
     * Modifies all attributes except for the exception
     *
     * @param game is the played game
     * @param active true if the player is active
     * @param timeLeftToCompleteTask is how much time the player has to complete the task
     * @param privateObjectives is the array with all possible private colors of the player
     * @param nextAction is the action that the player must do
     * @param nextActionInfo is the next action hat the player has to do
     */
    public UpdatedGameResponse(ClientGame game, ClientColor[] privateObjectives, boolean active, Integer timeLeftToCompleteTask, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo) {
        this(game, active, timeLeftToCompleteTask, privateObjectives, nextAction, nextActionInfo, null);
    }

    /**
     * @param exception is the exception that could be thrown by the
     */
    public UpdatedGameResponse(Exception exception) {
        this(null, false, null, null, null, null, exception);
    }


    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}