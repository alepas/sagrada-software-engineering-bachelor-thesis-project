package it.polimi.ingsw.shared.network.commands.responses;

import it.polimi.ingsw.shared.clientInfo.ClientRoundTrack;

public class UpdatedRoundTrackResponse implements Response {
    public final ClientRoundTrack roundTrack;
    public final Exception exception;

    public UpdatedRoundTrackResponse(ClientRoundTrack roundTrack) {
        this.roundTrack = roundTrack;
        this.exception=null;
    }

    public UpdatedRoundTrackResponse(Exception exception) {
        this.exception = exception;
        this.roundTrack=null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}