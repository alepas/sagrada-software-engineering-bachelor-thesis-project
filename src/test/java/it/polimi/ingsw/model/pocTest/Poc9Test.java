package it.polimi.ingsw.model.pocTest;


import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.PublicObjectiveCard9;
import it.polimi.ingsw.model.constants.POCConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.Wpc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Poc9Test {

    private PublicObjectiveCard9 card;
    private Wpc wpc;
    private ArrayList<Dice> wpcDices = new ArrayList<>();
    private ArrayList<Dice> col0Dices = new ArrayList<>();
    private ArrayList<Dice> col1Dices = new ArrayList<>();
    private ArrayList<Dice> col2Dices = new ArrayList<>();
    private ArrayList<Dice> col3Dices = new ArrayList<>();
    private ArrayList<Dice> col4Dices = new ArrayList<>();
    private ArrayList<Dice> row0Dices = new ArrayList<>();
    private ArrayList<Dice> row1Dices = new ArrayList<>();
    private ArrayList<Dice> row2Dices = new ArrayList<>();
    private ArrayList<Dice> row3Dices = new ArrayList<>();

    @Before
    public void before(){
        card = new PublicObjectiveCard9();

        wpc = mock(Wpc.class);



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

        Dice dice18 = mock(Dice.class);
        when(dice18.getDiceColor()).thenReturn(Color.GREEN);
        when(dice18.getDiceNumber()).thenReturn(4);
        wpcDices.add(dice18);

        Dice dice19 = mock(Dice.class);
        when(dice19.getDiceColor()).thenReturn(Color.BLUE);
        when(dice19.getDiceNumber()).thenReturn(6);
        wpcDices.add(dice19);

        when(wpc.getWpcDices()).thenReturn(wpcDices);

        col0Dices.clear();
        col0Dices.add(dice0);
        col0Dices.add(dice5);
        col0Dices.add(dice10);
        col0Dices.add(dice15);
        when(wpc.getColDices(0)).thenReturn(col0Dices);

        col1Dices.clear();
        col1Dices.add(dice1);
        col1Dices.add(dice6);
        col1Dices.add(dice11);
        col1Dices.add(dice16);
        when(wpc.getColDices(1)).thenReturn(col1Dices);

        col2Dices.clear();
        col2Dices.add(dice2);
        col2Dices.add(dice7);
        col2Dices.add(dice12);
        col2Dices.add(dice17);
        when(wpc.getColDices(2)).thenReturn(col2Dices);

        col3Dices.clear();
        col3Dices.add(dice3);
        col3Dices.add(dice8);
        col3Dices.add(dice13);
        col3Dices.add(dice18);
        when(wpc.getColDices(3)).thenReturn(col3Dices);

        col4Dices.clear();
        col4Dices.add(dice4);
        col4Dices.add(dice9);
        col4Dices.add(dice14);
        col4Dices.add(dice19);
        when(wpc.getColDices(4)).thenReturn(col4Dices);

        row0Dices.clear();
        row0Dices.add(dice0);
        row0Dices.add(dice1);
        row0Dices.add(dice2);
        row0Dices.add(dice3);
        row0Dices.add(dice4);
        when(wpc.getRowDices(0)).thenReturn(row0Dices);

        row1Dices.clear();
        row1Dices.add(dice5);
        row1Dices.add(dice6);
        row1Dices.add(dice7);
        row1Dices.add(dice8);
        row1Dices.add(dice9);
        when(wpc.getRowDices(1)).thenReturn(row1Dices);

        row2Dices.clear();
        row2Dices.add(dice10);
        row2Dices.add(dice11);
        row2Dices.add(dice12);
        row2Dices.add(dice13);
        row2Dices.add(dice14);
        when(wpc.getRowDices(2)).thenReturn(row2Dices);

        row3Dices.clear();
        row3Dices.add(dice15);
        row3Dices.add(dice16);
        row3Dices.add(dice17);
        row3Dices.add(dice18);
        row3Dices.add(dice19);
        when(wpc.getRowDices(3)).thenReturn(row3Dices);


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
    public void poc9Test(){
        Assert.assertEquals(9*POCConstants.POC9_SCORE, card.calculateScore(wpc));
    }
}
