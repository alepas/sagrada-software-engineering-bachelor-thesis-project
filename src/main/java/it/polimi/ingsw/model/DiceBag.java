package it.polimi.ingsw.model;

public class DiceBag {
    private int yellowDices;
    private int redDices;
    private int blueDices;
    private int greenDices;
    private int violetDices;
    private final int initialDiceNum = 18;

    /*annotazione importante: DicesExtraction deve essere un metodo del game, in questo modo elimino i due costruttori
     e anche l'array extracedDices; davide nella classe game inserisce un set (credo non sia la scelta migliore in quanto
     in un set non possono essereci due o più lementi uguali tra di loro mentre nel nostro gioco tale evento è possibile)
     io l'avevo pensato come un array: dobbiamo scegliere quale via seguire
     */

    //quando costruisco il sacco nella partita multiplayer passerò al costruttore
    // l'int numDices =2*numplayers +1
    private void DiceBag( ){
        yellowDices = initialDiceNum;
        redDices = initialDiceNum;
        blueDices = initialDiceNum;
        greenDices = initialDiceNum;
        violetDices = initialDiceNum;
    }

    public Dice[] DicesExtraction(int numPlayers){
        int numOfDices;
        if(numPlayers == 1)
            numOfDices = 4;
        else
            numOfDices = numPlayers*2 +1;
        Dice[] extractedDices = new Dice[numOfDices];
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
        return extractedDices;
    }

    public Dice pickDice(){
        Color color = selectedColour();
        return new Dice(color);
    }

    //seleziono in modo randomico il colore e controllo che siano ancora dispoibili dei dadi di quel colore
    private Color selectedColour(){
        Color color =  Color.randomColour();
        if(checkColour(color))
            return color;
        else
            return selectedColour();
    }

    //nell enum ogni val è associato ad un valore numerico tramite color.ordinal() recupero tale valore
    //utile per fare il check
    private boolean checkColour(Color color){
        if (color == Color.VIOLET){
            if (violetDices > 0){
                violetDices --;
                return true;
            }
            else
                return  false;
        }
        else if (color == Color.BLUE){
            if (blueDices > 0){
                blueDices --;
                return true;
            }
            else
                return false;
        }
        else if (color == Color.RED){
            if (redDices > 0){
                redDices --;
                return true;
            }
            else
                return false;
        }
        else if (color == Color.YELLOW){
            if (yellowDices > 0){
                yellowDices --;
                return true;
            }
            else
                return false;
        }
        else if (color == Color.GREEN){
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

