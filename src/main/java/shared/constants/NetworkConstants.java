package shared.constants;

import org.json.simple.JSONObject;
import shared.configLoader.SharedConfigLoader;

/**
 * This class contains all constants related to the network
 */
public class NetworkConstants {
    private static final JSONObject jsonSharedObject = SharedConfigLoader.jsonSharedObject;
    private static final JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

    public static final String SERVER_ADDRESS = (String) jsonNetwork.get("SERVER_ADDRESS");

    public static final int SOCKET_SERVER_PORT = ((Long) jsonNetwork.get("SOCKET_SERVER_PORT")).intValue();

    public static final int RMI_SERVER_PORT = ((Long) jsonNetwork.get("RMI_SERVER_PORT")).intValue();
    public static final String RMI_CONTROLLER_NAME = (String) jsonNetwork.get("RMI_CONTROLLER_NAME");
    public static final int RMI_INITIAL_POLLING_TIME = ((Long) jsonNetwork.get("RMI_INITIAL_POLLING_TIME")).intValue();
    public static final int RMI_POLLING_TIME = ((Long) jsonNetwork.get("RMI_POLLING_TIME")).intValue();
    public static final int RMI_MAX_POLLS_MISSED = ((Long) jsonNetwork.get("RMI_MAX_POLLS_MISSED")).intValue();

}
