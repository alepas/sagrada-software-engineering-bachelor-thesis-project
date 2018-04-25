package it.polimi.ingsw.model.pocTest;

import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.PublicObjectiveCard1;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Poc1Test {
    private PublicObjectiveCard1 card;
    private WPC wpc;
    private Dice[] wpcDices;
    private Dice[] colDices = new Dice[4];
    private Dice[] colDices2 = new Dice[3];
    private Dice[] rowDices = new Dice[5];
    private Dice[] rowDices2 = new Dice[3];

    @Before
    public void before(){
        card = new PublicObjectiveCard1();

        wpc = mock(WPC.class);
        wpcDices = new Dice[18];
        for(int i = 0; i < wpcDices.length; i++){
            wpcDices[i] = mock(Dice.class);
        }

        when(wpcDices[0].getDiceColor()).thenReturn(Color.RED);
        when(wpcDices[0].getDiceNumber()).thenReturn(3);

        when(wpcDices[1].getDiceColor()).thenReturn(Color.VIOLET);
        when(wpcDices[1].getDiceNumber()).thenReturn(4);

        when(wpcDices[2].getDiceColor()).thenReturn(Color.YELLOW);
        when(wpcDices[2].getDiceNumber()).thenReturn(1);

        when(wpcDices[3].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[3].getDiceNumber()).thenReturn(5);

        when(wpcDices[4].getDiceColor()).thenReturn(Color.BLUE);
        when(wpcDices[4].getDiceNumber()).thenReturn(6);




        when(wpcDices[5].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[5].getDiceNumber()).thenReturn(5);

        when(wpcDices[6].getDiceColor()).thenReturn(Color.RED);
        when(wpcDices[6].getDiceNumber()).thenReturn(3);

        when(wpcDices[7].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[7].getDiceNumber()).thenReturn(4);

        when(wpcDices[8].getDiceColor()).thenReturn(Color.RED);
        when(wpcDices[8].getDiceNumber()).thenReturn(2);

        when(wpcDices[9].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[9].getDiceNumber()).thenReturn(6);




        when(wpcDices[10].getDiceColor()).thenReturn(Color.VIOLET);
        when(wpcDices[10].getDiceNumber()).thenReturn(1);

        when(wpcDices[11].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[11].getDiceNumber()).thenReturn(4);

        when(wpcDices[12].getDiceColor()).thenReturn(Color.BLUE);
        when(wpcDices[12].getDiceNumber()).thenReturn(6);

        when(wpcDices[13].getDiceColor()).thenReturn(Color.YELLOW);
        when(wpcDices[13].getDiceNumber()).thenReturn(1);

        when(wpcDices[14].getDiceColor()).thenReturn(Color.RED);
        when(wpcDices[14].getDiceNumber()).thenReturn(2);




        when(wpcDices[15].getDiceColor()).thenReturn(Color.YELLOW);
        when(wpcDices[15].getDiceNumber()).thenReturn(4);

        when(wpcDices[16].getDiceColor()).thenReturn(Color.GREEN);
        when(wpcDices[16].getDiceNumber()).thenReturn(4);

        when(wpcDices[17].getDiceColor()).thenReturn(Color.BLUE);
        when(wpcDices[17].getDiceNumber()).thenReturn(6);

        when(wpc.getWpcDices()).thenReturn(wpcDices);

        colDices[0] = wpcDices[0];
        colDices[1] = wpcDices[5];
        colDices[2] = wpcDices[10];
        colDices[3] = wpcDices[15];
        when(wpc.getColDices(0)).thenReturn(colDices);

        colDices2[0] = wpcDices[1];
        colDices2[1] = wpcDices[6];
        colDices2[2] = wpcDices[11];
        when(wpc.getColDices(1)).thenReturn(colDices2);

        colDices[0] = wpcDices[2];
        colDices[1] = wpcDices[7];
        colDices[2] = wpcDices[12];
        colDices[3] = wpcDices[16];
        when(wpc.getColDices(2)).thenReturn(colDices);

        colDices[0] = wpcDices[3];
        colDices[1] = wpcDices[8];
        colDices[2] = wpcDices[13];
        colDices[3] = wpcDices[17];
        when(wpc.getColDices(3)).thenReturn(colDices);

        colDices2[0] = wpcDices[4];
        colDices2[1] = wpcDices[9];
        colDices2[2] = wpcDices[14];
        when(wpc.getColDices(4)).thenReturn(colDices2);

        rowDices[0] = wpcDices[0];
        rowDices[1] = wpcDices[1];
        rowDices[2] = wpcDices[2];
        rowDices[3] = wpcDices[3];
        rowDices[4] = wpcDices[4];
        when(wpc.getRowDices(0)).thenReturn(rowDices);

        rowDices[0] = wpcDices[5];
        rowDices[1] = wpcDices[6];
        rowDices[2] = wpcDices[7];
        rowDices[3] = wpcDices[8];
        rowDices[4] = wpcDices[9];
        when(wpc.getRowDices(1)).thenReturn(rowDices);

        rowDices[0] = wpcDices[10];
        rowDices[1] = wpcDices[11];
        rowDices[2] = wpcDices[12];
        rowDices[3] = wpcDices[13];
        rowDices[4] = wpcDices[14];
        when(wpc.getRowDices(2)).thenReturn(rowDices);

        rowDices2[0] = wpcDices[15];
        rowDices2[1] = wpcDices[16];
        rowDices2[2] = wpcDices[17];
        when(wpc.getRowDices(3)).thenReturn(rowDices2);


        when(wpc.numDicesOfColor(Color.BLUE)).thenReturn(3);
        when(wpc.numDicesOfColor(Color.RED)).thenReturn(4);
        when(wpc.numDicesOfColor(Color.YELLOW)).thenReturn(3);
        when(wpc.numDicesOfColor(Color.VIOLET)).thenReturn(2);
        when(wpc.numDicesOfColor(Color.GREEN)).thenReturn(6);

        when(wpc.numDicesOfShade(1)).thenReturn(3);
        when(wpc.numDicesOfShade(2)).thenReturn(2);
        when(wpc.numDicesOfShade(3)).thenReturn(2);
        when(wpc.numDicesOfShade(4)).thenReturn(5);
        when(wpc.numDicesOfShade(5)).thenReturn(2);
        when(wpc.numDicesOfShade(6)).thenReturn(4);
    }

    @Test
    public void poc1Test(){
        Assert.assertEquals(2*POCConstants.POC1_SCORE, card.calculateScore(wpc));
    }
}
