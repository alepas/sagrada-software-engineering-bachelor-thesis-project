package it.polimi.ingsw.model.constants;

/**
 * This class contains all constants related to the public objective cards;
 * for each card there are four constants:
 * - the id which is a numeric string
 * - the name given by the game
 * - the description which is used to explain to the player what can be done by activating it
 */
public class ToolCardConstants {

    //ToolCard1
    public static final String TOOLCARD1_ID = "1";
    public static final String TOOL1_NAME = "Pinza Sgrossatrice";
    public static final String TOOL1_DESCRIPTION = "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1.\n" +
            "Non puoi cambiare un 6 in 1 o un 1 in 6.";

    //ToolCard2
    public static final String TOOLCARD2_ID = "2";
    public static final String TOOL2_NAME = "Pennello per Eglomise";
    public static final String TOOL2_DESCRIPTION = "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore.\n" +
            "Devi rispettare tutte le altre restrizioni di piazzamento";

    //ToolCard3
    public static final String TOOLCARD3_ID = "3";
    public static final String TOOL3_NAME = "Alesatore per lamina di rame";
    public static final String TOOL3_DESCRIPTION = "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore\n" +
            "Devi rispettare tutte le altre restrizioni di piazzamento";

    //ToolCard4
    public static final String TOOLCARD4_ID = "4";
    public static final String TOOL4_NAME = "Lathekin";
    public static final String TOOL4_DESCRIPTION = "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento";

    //ToolCard5
    public static final String TOOLCARD5_ID = "5";
    public static final String TOOL5_NAME = "Taglierina circolare";
    public static final String TOOL5_DESCRIPTION = "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei Round";

    //ToolCard6
    public static final String TOOLCARD6_ID = "6";
    public static final String TOOL6_NAME = "Pennello per Pasta Salda";
    public static final String TOOL6_DESCRIPTION = "Dopo aver scelto un dado, tira nuovamente quel dado\n" +
            "Se non puoi piazzarlo, riponilo nella Riserva";

    //ToolCard7
    public static final String TOOLCARD7_ID = "7";
    public static final String TOOL7_NAME = "Martelletto";
    public static final String TOOL7_DESCRIPTION = "Tira nuovamente tutti i dadi della Riserva\n" +
            "Questa carta può essera usata solo durante il tuo secondo turno, prima di scegliere il secondo dado";

    //ToolCard8
    public static final String TOOLCARD8_ID = "8";
    public static final String TOOL8_NAME = "Tenaglia a Rotelle";
    public static final String TOOL8_DESCRIPTION = "Dopo il tuo primo turno scegli immediatamente un altro dado\n" +
            "Salta il tuo secondo turno in questo round";

    //ToolCard9
    public static final String TOOLCARD9_ID = "9";
    public static final String TOOL9_NAME = "Riga in Sughero";
    public static final String TOOL9_DESCRIPTION = "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente a un altro dado\n" +
            "Devi rispettare tutte le restrizioni di piazzamento";

    //ToolCard10
    public static final String TOOLCARD10_ID = "10";
    public static final String TOOL10_NAME = "Tampone Diamantato";
    public static final String TOOL10_DESCRIPTION = "Dopo aver scelto un dado, giralo sulla faccia opposta\n" +
            "6 diventa 1, 5 diventa 2, 4 diventa 3 ecc.";

    //ToolCard11
    public static final String TOOLCARD11_ID = "11";
    public static final String TOOL11_NAME = "Diluente per Pasta Salda";
    public static final String TOOL11_DESCRIPTION = "Dopo aver scelto un dado, riponilo nel Sacchetto, poi pescane uno dal Sacchetto\n" +
            "Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento";

    //ToolCard12
    public static final String TOOLCARD12_ID = "12";
    public static final String TOOL12_NAME = "Taglierina Manuale";
    public static final String TOOL12_DESCRIPTION = "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round\n" +
            "Devi rispettare tutte le restrizioni di piazzamento";
}
