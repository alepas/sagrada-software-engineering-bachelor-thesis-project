package shared.network.commands.requests;

import shared.clientInfo.ToolCardInteruptValues;
import shared.network.commands.responses.Response;

public class ToolCardInteruptRequest implements Request {
    public final String userToken;
    public final ToolCardInteruptValues value;

    public ToolCardInteruptRequest(String userToken, ToolCardInteruptValues value) {
        this.userToken = userToken;
        this.value = value;
    }



    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
