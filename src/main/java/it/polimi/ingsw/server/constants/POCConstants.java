package it.polimi.ingsw.server.constants;

/**
 * This class contains all constants related to the public objective cards;
 * for each card there are four constants:
 * - the id which is a numeric string
 * - the amount of points that will be assigned to the player each time he/she will be able to reach the goal
 * - the name
 * - the description which is used to explain to the player what to do to gain the POC points
 */
public class POCConstants {

    //PublicObjectiveCard1
    public static final String POC1_ID = "1";
    public static final int POC1_SCORE = 6;
    public static final String POC1_NAME = "Colori diversi - Riga";
    public static final String POC1_DESCRIPTION = "Ottieni " + POC1_SCORE + " punti per ogni riga " +
            "senza colori ripetuti";

    //PublicObjectiveCard2
    public static final String POC2_ID = "2";
    public static final int POC2_SCORE = 5;
    public static final String POC2_NAME = "Colori diversi - Colonna";
    public static final String POC2_DESCRIPTION = "Ottieni " + POC2_SCORE + " punti per ogni colonna " +
            "senza colori ripetuti";

    //PublicObjectiveCard3
    public static final String POC3_ID = "3";
    public static final int POC3_SCORE = 5;
    public static final String POC3_NAME = "Sfumature diverse - Riga";
    public static final String POC3_DESCRIPTION = "Ottieni " + POC3_SCORE + " punti per ogni riga " +
            "senza sfumature ripetute";

    //PublicObjectiveCard4
    public static final String POC4_ID = "4";
    public static final int POC4_SCORE = 4;
    public static final String POC4_NAME = "Sfumature diverse - Colonna";
    public static final String POC4_DESCRIPTION = "Ottieni " + POC4_SCORE + " punti per ogni colonna " +
            "senza sfumature ripetute";

    //PublicObjectiveCard5
    public static final String POC5_ID = "5";
    public static final int POC5_SCORE = 2;
    public static final String POC5_NAME = "Sfumature chiare";
    public static final String POC5_DESCRIPTION = "Ottieni " + POC5_SCORE + " punti per ogni set " +
            "di 1 e 2";

    //PublicObjectiveCard6
    public static final String POC6_ID = "6";
    public static final int POC6_SCORE = 2;
    public static final String POC6_NAME = "Sfumature medie";
    public static final String POC6_DESCRIPTION = "Ottieni " + POC6_SCORE + " punti per ogni set " +
            "di 3 e 4";

    //PublicObjectiveCard7
    public static final String POC7_ID = "7";
    public static final int POC7_SCORE = 2;
    public static final String POC7_NAME = "Sfumature scure";
    public static final String POC7_DESCRIPTION = "Ottieni " + POC7_SCORE + " punti per ogni set " +
            "di 5 e 6";

    //PublicObjectiveCard8
    public static final String POC8_ID = "8";
    public static final int POC8_SCORE = 5;
    public static final String POC8_NAME = "Sfumature diverse";
    public static final String POC8_DESCRIPTION = "Ottieni " + POC8_SCORE + " punti per ogni set " +
            "di dadi di ogni valore";

    //PublicObjectiveCard9
    public static final String POC9_ID = "9";
    public static final int POC9_SCORE = 1;
    public static final String POC9_NAME = "Diagonali colorate";
    public static final String POC9_DESCRIPTION = "Ottieni punti pari al numero di dadi " +
            "dello stesso colore diagonalmente adiacenti";

    //PublicObjectiveCard10
    public static final String POC10_ID = "10";
    public static final int POC10_SCORE = 4;
    public static final String POC10_NAME = "Varietà di colore";
    public static final String POC10_DESCRIPTION = "Ottieni " + POC10_SCORE + " punti per ogni set " +
            "di dadi di ogni colore";
}
