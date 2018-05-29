package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

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
