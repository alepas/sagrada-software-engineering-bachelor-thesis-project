package it.polimi.ingsw.model.dicebag;

import static it.polimi.ingsw.model.constants.DiceBagCostants.INITIAL_DICE_NUMBER;

public class DiceBag {
    private int yellowDices;
    private int redDices;
    private int blueDices;
    private int greenDices;
    private int violetDices;

    /*annotazione importante: DicesExtraction deve essere un metodo del game, in questo modo elimino i due costruttori
     e anche l'array extracedDices; davide nella classe game inserisce un set (credo non sia la scelta migliore in quanto
     in un set non possono essereci due o più lementi uguali tra di loro mentre nel nostro gioco tale evento è possibile)
     io l'avevo pensato come un array: dobbiamo scegliere quale via seguire
     */

    //quando costruisco il sacco nella partita multiplayer passerò al costruttore
    // l'int numDices =2*numplayers +1
    public DiceBag(){
        yellowDices = INITIAL_DICE_NUMBER;
        redDices = INITIAL_DICE_NUMBER;
        blueDices = INITIAL_DICE_NUMBER;
        greenDices = INITIAL_DICE_NUMBER;
        violetDices = INITIAL_DICE_NUMBER;
    }

    public Dice[] DicesExtraction(int numPlayers){
        int numOfDices;
        if(numPlayers == 1)
            numOfDices = 4; //costante da inserire in class costante
        else
            numOfDices = numPlayers*2 +1;
        Dice[] extractedDices = new Dice[numOfDices];
        for (int i = 0; i < extractedDices.length; i++)
            extractedDices[i] = pickDice();
        return extractedDices;
    }

    private Dice pickDice() {
        Color color;
        do {
            color = Color.randomColor();
        } while(!checkColor(color));
        switch (color){
            case RED: redDices--;
            case BLUE: blueDices--;
            case GREEN: greenDices--;
            case VIOLET: violetDices--;
            case YELLOW: yellowDices--;
        }
        return new Dice(color);
    }

    //nell enum ogni val è associato ad un valore numerico tramite color.ordinal() recupero tale valore
    //utile per fare il check
    private boolean checkColor(Color color) throws EnumConstantNotPresentException{
        if (color == Color.VIOLET)
            return (violetDices > 0);
        else if (color == Color.BLUE)
            return (blueDices > 0);
        else if (color == Color.RED)
            return (redDices > 0);
        else if (color == Color.YELLOW)
            return (yellowDices > 0);
        else
            return (greenDices > 0);
    }

     public int getYellowDices(){return yellowDices;}
     public int getRedDices(){return redDices;}
     public int getGreenDices(){return greenDices;}
     public int getBlueDices(){return blueDices;}
     public int getVioletDices(){return violetDices;}
}

