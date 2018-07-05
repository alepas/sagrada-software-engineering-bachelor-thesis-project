package shared.network.commands.responses;

import java.io.Serializable;

/**
 * Interface which contains the only response handler method
 */
public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}
