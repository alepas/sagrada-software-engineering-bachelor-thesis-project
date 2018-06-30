package shared.network.commands.requests;

import shared.network.commands.responses.Response;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}