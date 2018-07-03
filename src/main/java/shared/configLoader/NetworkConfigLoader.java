package shared.configLoader;

import org.json.simple.JSONObject;

import static shared.constants.NetworkConstants.*;

public class NetworkConfigLoader {

    public static void loadConfig(JSONObject jsonNetwork) {
        SERVER_ADDRESS = (String) jsonNetwork.get("SERVER_ADDRESS");
        SOCKET_SERVER_PORT = ((Long) jsonNetwork.get("SOCKET_SERVER_PORT")).intValue();
        RMI_SERVER_PORT = ((Long) jsonNetwork.get("RMI_SERVER_PORT")).intValue();
        RMI_CONTROLLER_NAME = (String) jsonNetwork.get("RMI_CONTROLLER_NAME");
        RMI_INITIAL_POLLING_TIME = ((Long) jsonNetwork.get("RMI_INITIAL_POLLING_TIME")).intValue();
        RMI_POLLING_TIME = ((Long) jsonNetwork.get("RMI_POLLING_TIME")).intValue();
        RMI_MAX_POLLS_MISSED = ((Long) jsonNetwork.get("RMI_MAX_POLLS_MISSED")).intValue();
    }
}
