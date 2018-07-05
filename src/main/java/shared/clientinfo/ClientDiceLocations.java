package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the diceLocation enum in the server model, it doesn't contains any logic
 */
public enum ClientDiceLocations implements Serializable {
    WPC, ROUNDTRACK, EXTRACTED, DICEBAG
}
