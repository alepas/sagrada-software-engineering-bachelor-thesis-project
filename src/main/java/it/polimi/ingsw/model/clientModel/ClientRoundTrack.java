package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.model.dicebag.Dice;

import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.RoundTrackConstants.HYPOTHETICAL_MAX_DICES_PER_ROUND;
import static it.polimi.ingsw.model.constants.RoundTrackConstants.NUM_OF_ROUND;

public class ClientRoundTrack implements Serializable {
    private int currentRound;
    private ClientDice[][] dicesNotUsed;

    //array di arraylist in questo modo posso aggiungere tutti i dadi che voglio in modo dinamico
    public ClientRoundTrack(ClientDice[][] roundTrack){
        dicesNotUsed = roundTrack;

    }


    public ClientDice[][] getAllDices(){
        return dicesNotUsed;
    }

}
