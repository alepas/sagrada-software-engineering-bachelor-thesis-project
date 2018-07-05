package shared.network.commands.requests;

import shared.clientInfo.ToolCardInterruptValues;
import shared.network.commands.responses.Response;

public class ToolCardInteruptRequest implements Request {
    public final String userToken;
    public final ToolCardInterruptValues value;

    public ToolCardInteruptRequest(String userToken, ToolCardInterruptValues value) {
        this.userToken = userToken;
        this.value = value;
    }



    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
