package shared.network.commands.responses;

import shared.clientinfo.ClientRoundTrack;

public class UpdatedRoundTrackResponse implements Response {
    public final ClientRoundTrack roundTrack;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param roundTrack is the client roundTrack associated to the game
     */
    public UpdatedRoundTrackResponse(ClientRoundTrack roundTrack) {
        this.roundTrack = roundTrack;
        this.exception=null;
    }

    /**
     * Constructor which contains only the exception.
     *
     * @param exception is the exception that could be thrown
     */
    public UpdatedRoundTrackResponse(Exception exception) {
        this.exception = exception;
        this.roundTrack=null;
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