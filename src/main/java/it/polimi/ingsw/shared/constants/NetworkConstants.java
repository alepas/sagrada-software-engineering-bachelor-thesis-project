package it.polimi.ingsw.shared.constants;

/**
 * This class contains all constants related to the network
 */
public class NetworkConstants {
    public static final String SERVER_ADDRESS = "localhost";

    public static final int SOCKET_SERVER_PORT = 8000;

    public static final int RMI_SERVER_PORT = 1099;
    public static final String RMI_CONTROLLER_NAME = "controller";
    public static final int RMI_INITIAL_POLLING_TIME = 3000;
    public static final int RMI_POLLING_TIME = 300;             //Time is in miliseconds
    public static final int RMI_MAX_POLLS_MISSED = 3;

}
