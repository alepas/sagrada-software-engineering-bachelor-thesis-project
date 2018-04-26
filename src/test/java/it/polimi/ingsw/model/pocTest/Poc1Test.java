package it.polimi.ingsw.model.pocTest;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.PublicObjectiveCard1;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Poc1Test {
    private PublicObjectiveCard1 card;
    private WPC wpc;
    private ArrayList<Dice> wpcDices = new ArrayList<>();
    private ArrayList<Dice> colDices = new ArrayList<>();
    private ArrayList<Dice> rowDices = new ArrayList<>();

    @Before
    public void before(){
        card = new PublicObjectiveCard1();

        wpc = mock(WPC.class);



        Dice dice0 = mock(Dice.class);
        when(dice0.getDiceColor()).thenReturn(Color.RED);
        when(dice0.getDiceNumber()).thenReturn(3);
        wpcDices.add(dice0);

        Dice dice1 = mock(Dice.class);
        when(dice1.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice1.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice1);

        Dice dice2 = mock(Dice.class);
        when(dice2.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice2.getDiceNumber()).thenReturn(1);
        wpcDices.add(dice2);

        Dice dice3 = mock(Dice.class);
        when(dice3.getDiceColor()).thenReturn(Color.GREEN);
        when(dice3.getDiceNumber()).thenReturn(5);
        wpcDices.add(dice3);

        Dice dice4 = mock(Dice.class);
        when(dice4.getDiceColor()).thenReturn(Color.BLUE);
        when(dice4.getDiceNumber()).thenReturn(6);
        wpcDices.add(dice4);




        Dice dice5 = mock(Dice.class);
        when(dice5.getDiceColor()).thenReturn(Color.GREEN);
        when(dice5.getDiceNumber()).thenReturn(5);
        wpcDices.add(dice5);

        Dice dice6 = mock(Dice.class);
        when(dice6.getDiceColor()).thenReturn(Color.RED);
        when(dice6.getDiceNumber()).thenReturn(3);
        wpcDices.add(dice6);

        Dice dice7 = mock(Dice.class);
        when(dice7.getDiceColor()).thenReturn(Color.GREEN);
        when(dice7.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice7);

        Dice dice8 = mock(Dice.class);
        when(dice8.getDiceColor()).thenReturn(Color.RED);
        when(dice8.getDiceNumber()).thenReturn(2);
        wpcDices.add(dice8);

        Dice dice9 = mock(Dice.class);
        when(dice9.getDiceColor()).thenReturn(Color.GREEN);
        when(dice9.getDiceNumber()).thenReturn(6);
        wpcDices.add(dice9);




        Dice dice10 = mock(Dice.class);
        when(dice10.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice10.getDiceNumber()).thenReturn(1);
        wpcDices.add(dice10);

        Dice dice11 = mock(Dice.class);
        when(dice11.getDiceColor()).thenReturn(Color.GREEN);
        when(dice11.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice11);

        Dice dice12 = mock(Dice.class);
        when(dice12.getDiceColor()).thenReturn(Color.BLUE);
        when(dice12.getDiceNumber()).thenReturn(6);
        wpcDices.add(dice12);

        Dice dice13 = mock(Dice.class);
        when(dice13.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice13.getDiceNumber()).thenReturn(1);
        wpcDices.add(dice13);

        Dice dice14 = mock(Dice.class);
        when(dice14.getDiceColor()).thenReturn(Color.RED);
        when(dice14.getDiceNumber()).thenReturn(2);
        wpcDices.add(dice14);




        Dice dice15 = mock(Dice.class);
        when(dice15.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice15.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice15);

        Dice dice16 = mock(Dice.class);
        when(dice16.getDiceColor()).thenReturn(Color.GREEN);
        when(dice16.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice16);

        Dice dice17 = mock(Dice.class);
        when(dice17.getDiceColor()).thenReturn(Color.BLUE);
        when(dice17.getDiceNumber()).thenReturn(6);
        wpcDices.add(dice17);

        when(wpc.getWpcDices()).thenReturn(wpcDices);

        colDices.clear();
        colDices.add(dice0);
        colDices.add(dice5);
        colDices.add(dice10);
        colDices.add(dice15);
        when(wpc.getColDices(0)).thenReturn(colDices);

        colDices.clear();
        colDices.add(dice1);
        colDices.add(dice6);
        colDices.add(dice11);
        when(wpc.getColDices(1)).thenReturn(colDices);

        colDices.clear();
        colDices.add(dice2);
        colDices.add(dice7);
        colDices.add(dice12);
        colDices.add(dice16);
        when(wpc.getColDices(2)).thenReturn(colDices);

        colDices.clear();
        colDices.add(dice3);
        colDices.add(dice8);
        colDices.add(dice13);
        colDices.add(dice17);
        when(wpc.getColDices(3)).thenReturn(colDices);

        colDices.clear();
        colDices.add(dice4);
        colDices.add(dice9);
        colDices.add(dice14);
        when(wpc.getColDices(4)).thenReturn(colDices);

        rowDices.clear();
        rowDices.add(dice0);
        rowDices.add(dice1);
        rowDices.add(dice2);
        rowDices.add(dice3);
        rowDices.add(dice4);
        when(wpc.getRowDices(0)).thenReturn(rowDices);

        rowDices.clear();
        rowDices.add(dice5);
        rowDices.add(dice6);
        rowDices.add(dice7);
        rowDices.add(dice8);
        rowDices.add(dice9);
        when(wpc.getRowDices(1)).thenReturn(rowDices);

        rowDices.clear();
        rowDices.add(dice10);
        rowDices.add(dice11);
        rowDices.add(dice12);
        rowDices.add(dice13);
        rowDices.add(dice14);
        when(wpc.getRowDices(2)).thenReturn(rowDices);

        rowDices.clear();
        rowDices.add(dice15);
        rowDices.add(dice16);
        rowDices.add(dice17);
        when(wpc.getRowDices(3)).thenReturn(rowDices);


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
