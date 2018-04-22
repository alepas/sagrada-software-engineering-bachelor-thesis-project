package it.polimi.ingsw.model;
import java.lang.reflect.Array;
import java.util.*;
import it.polimi.ingsw.model.Dice;

public class DiceBag {
    private int yellowDices;
    private int redDices;
    private int blueDices;
    private int greenDices;
    private int violetDices;
    private Dice[] extractedDices;
    private final int initialDiceNum = 18;

    /*annotazione importante: DicesExtraction deve essere un metodo del game, in questo modo elimino i due costruttori
     e anche l'array extracedDices; davide nella classe game inserisce un set (credo non sia la scelta migliore in quanto
     in un set non possono essereci due o più lementi uguali tra di loro mentre nel nostro gioco tale evento è possibile)
     io l'avevo pensato come un array: dobbiamo scegliere quale via seguire
     */
    //quando costruisco il sacco nella partita multiplayer passerò al costruttore
    // l'int numDices =2*numplayers +1
    private void DiceBag(int numDices){
        yellowDices = initialDiceNum;
        redDices = initialDiceNum;
        blueDices = initialDiceNum;
        greenDices = initialDiceNum;
        violetDices = initialDiceNum;
        Dice[] extractedDices = new Dice[numDices];
    }

    //costruttore soloPlayer: senza parametri
    private void DiceBag( ){
        yellowDices = initialDiceNum;
        redDices = initialDiceNum;
        blueDices = initialDiceNum;
        greenDices = initialDiceNum;
        violetDices = initialDiceNum;
        Dice[] extractedDices = new Dice[4];
    }
    //non sono sicura del suo funzionamento
    public void DicesExtraction(int numPlayers){
        if( numPlayers == 1) {
            for(int i=0; i < extractedDices.length; i++)
                extractedDices[i]= pickDice();
        }
        else if (numPlayers == 2){
            for(int i=0; i < extractedDices.length; i++)
                extractedDices[i]= pickDice();
        }
        else if (numPlayers == 3){
            for(int i=0; i < extractedDices.length; i++)
                extractedDices[i]= pickDice();
        }
        else {
            for (int i = 0; i < extractedDices.length; i++)
                extractedDices[i] = pickDice();
        }
    }

    public Dice pickDice(){
        Colour color = selectedColour();
        Dice dice = new Dice(color);
        return dice;
    }

    //seleziono in modo randomico il colore e controllo che siano ancora dispoibili dei dadi di quel colore
    private Colour selectedColour(){
        Colour colour =  Colour.randomColour();
        if(checkColour(colour))
            return colour;
        else
            return selectedColour();
    }

    //nell enum ogni val è associato ad un valore numerico tramite colour.ordinal() recupero tale valore
    //utile per fare il check
    private boolean checkColour(Colour colour){
        if (colour.ordinal() == 0){
            if (violetDices > 0){
                violetDices --;
                return true;
            }
            else
                return  false;
        }
        else if (colour.ordinal() == 1){
            if (blueDices > 0){
                blueDices --;
                return true;
            }
            else
                return false;
        }
        else if (colour.ordinal() == 2){
            if (redDices > 0){
                redDices --;
                return true;
            }
            else
                return false;
        }
        else if (colour.ordinal() == 3){
            if (yellowDices > 0){
                yellowDices --;
                return true;
            }
            else
                return false;
        }
        else if (colour.ordinal() == 4){
            if (greenDices > 0){
                greenDices --;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }
}

