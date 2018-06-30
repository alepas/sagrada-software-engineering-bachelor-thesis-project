package shared.network.commands.responses;

import java.io.Serializable;

public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}
