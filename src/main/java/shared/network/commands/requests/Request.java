package shared.network.commands.requests;

import shared.network.commands.responses.Response;

import java.io.Serializable;

/**
 * This interface contains the response handler method which is called by all responses.
 * this method is necessary to work with all parameters that are given by the answer.
 */
public interface Request extends Serializable {
    Response handle(RequestHandler handler);
}