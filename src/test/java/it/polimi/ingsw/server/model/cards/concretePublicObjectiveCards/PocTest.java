package it.polimi.ingsw.server.model.cards.concretePublicObjectiveCards;

import it.polimi.ingsw.server.model.cards.concretePublicObjectiveCards.*;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.wpc.Wpc;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.server.constants.POCConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PocTest {
    private PublicObjectiveCard1 card1;
    private PublicObjectiveCard2 card2;
    private PublicObjectiveCard3 card3;
    private PublicObjectiveCard4 card4;
    private PublicObjectiveCard5 card5;
    private PublicObjectiveCard6 card6;
    private PublicObjectiveCard7 card7;
    private PublicObjectiveCard8 card8;
    private PublicObjectiveCard9 card9;
    private PublicObjectiveCard10 card10;

    private Wpc wpc;
    private Wpc wpcEmpty;
    private ArrayList<Dice> emptyRow = new ArrayList<>();


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
    private ArrayList<Dice> row1DicesSpaces = new ArrayList<>();


    /**
     * Creates a mock wpc, not the schema but all dices and thei position in the schema
     */
    @Before
    public void before() {
        card1 = new PublicObjectiveCard1();
        card2 = new PublicObjectiveCard2();
        card3 = new PublicObjectiveCard3();
        card4 = new PublicObjectiveCard4();
        card5 = new PublicObjectiveCard5();
        card6 = new PublicObjectiveCard6();
        card7 = new PublicObjectiveCard7();
        card8 = new PublicObjectiveCard8();
        card9 = new PublicObjectiveCard9();
        card10 = new PublicObjectiveCard10();

        wpc = mock(Wpc.class);
        wpcEmpty = mock(Wpc.class);


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

/*        Dice dice8 = mock(Dice.class);
        when(dice8.getDiceColor()).thenReturn(Color.RED);
        when(dice8.getDiceNumber()).thenReturn(2);
        wpcDices.add(dice8);*/

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
        //col3Dices.add(dice8);
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
        when(wpc.getRowDicesAndEmptySpaces(0)).thenReturn(row0Dices);

        row1Dices.clear();
        row1Dices.add(dice5);
        row1Dices.add(dice6);
        row1Dices.add(dice7);
        //row1Dices.add(dice8);
        row1Dices.add(dice9);
        when(wpc.getRowDices(1)).thenReturn(row1Dices);

        row1DicesSpaces.clear();
        row1DicesSpaces.add(dice5);
        row1DicesSpaces.add(dice6);
        row1DicesSpaces.add(dice7);
        row1DicesSpaces.add(null);
        row1DicesSpaces.add(dice9);
        when(wpc.getRowDicesAndEmptySpaces(1)).thenReturn(row1DicesSpaces);


        row2Dices.clear();
        row2Dices.add(dice10);
        row2Dices.add(dice11);
        row2Dices.add(dice12);
        row2Dices.add(dice13);
        row2Dices.add(dice14);
        when(wpc.getRowDices(2)).thenReturn(row2Dices);
        when(wpc.getRowDicesAndEmptySpaces(2)).thenReturn(row2Dices);

        row3Dices.clear();
        row3Dices.add(dice15);
        row3Dices.add(dice16);
        row3Dices.add(dice17);
        row3Dices.add(dice18);
        row3Dices.add(dice19);
        when(wpc.getRowDices(3)).thenReturn(row3Dices);
        when(wpc.getRowDicesAndEmptySpaces(3)).thenReturn(row3Dices);


        when(wpc.numDicesOfColor(Color.BLUE)).thenReturn(4);
        when(wpc.numDicesOfColor(Color.RED)).thenReturn(3);
        when(wpc.numDicesOfColor(Color.YELLOW)).thenReturn(3);
        when(wpc.numDicesOfColor(Color.VIOLET)).thenReturn(2);
        when(wpc.numDicesOfColor(Color.GREEN)).thenReturn(7);

        when(wpc.numDicesOfShade(1)).thenReturn(3);
        when(wpc.numDicesOfShade(2)).thenReturn(1);
        when(wpc.numDicesOfShade(3)).thenReturn(2);
        when(wpc.numDicesOfShade(4)).thenReturn(6);
        when(wpc.numDicesOfShade(5)).thenReturn(2);
        when(wpc.numDicesOfShade(6)).thenReturn(5);


        when(wpcEmpty.getRowDices(0)).thenReturn(null);
        when(wpcEmpty.getRowDices(1)).thenReturn(null);
        when(wpcEmpty.getRowDices(2)).thenReturn(null);
        when(wpcEmpty.getRowDices(3)).thenReturn(null);
        when(wpcEmpty.getColDices(0)).thenReturn(null);
        when(wpcEmpty.getColDices(1)).thenReturn(null);
        when(wpcEmpty.getColDices(2)).thenReturn(null);
        when(wpcEmpty.getColDices(3)).thenReturn(null);
        when(wpcEmpty.getColDices(4)).thenReturn(null);
        when(wpcEmpty.getWpcDices()).thenReturn(null);

        when(wpcEmpty.numDicesOfColor(Color.BLUE)).thenReturn(0);
        when(wpcEmpty.numDicesOfColor(Color.RED)).thenReturn(0);
        when(wpcEmpty.numDicesOfColor(Color.YELLOW)).thenReturn(0);
        when(wpcEmpty.numDicesOfColor(Color.VIOLET)).thenReturn(0);
        when(wpcEmpty.numDicesOfColor(Color.GREEN)).thenReturn(0);

        when(wpcEmpty.numDicesOfShade(1)).thenReturn(0);
        when(wpcEmpty.numDicesOfShade(2)).thenReturn(0);
        when(wpcEmpty.numDicesOfShade(3)).thenReturn(0);
        when(wpcEmpty.numDicesOfShade(4)).thenReturn(0);
        when(wpcEmpty.numDicesOfShade(5)).thenReturn(0);
        when(wpcEmpty.numDicesOfShade(6)).thenReturn(0);


        emptyRow.add(null);
        emptyRow.add(null);
        emptyRow.add(null);
        emptyRow.add(null);
        emptyRow.add(null);


        when(wpcEmpty.getRowDicesAndEmptySpaces(0)).thenReturn(emptyRow);
        when(wpcEmpty.getRowDicesAndEmptySpaces(1)).thenReturn(emptyRow);
        when(wpcEmpty.getRowDicesAndEmptySpaces(2)).thenReturn(emptyRow);
        when(wpcEmpty.getRowDicesAndEmptySpaces(3)).thenReturn(emptyRow);


    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of all different colors in a same row.
     */
    @Test
    public void poc1Test() {
        assertEquals(2 * POC1_SCORE, card1.calculateScore(wpc));
        assertEquals(0, card1.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of all different colors in a same column.
     */
    @Test
    public void poc2Test() {
        assertEquals(POC2_SCORE, card2.calculateScore(wpc));
        assertEquals(0, card2.calculateScore(wpcEmpty));

    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of all different numbers in a same row.
     */
    @Test
    public void poc3Test() {
        assertEquals(POC3_SCORE, card3.calculateScore(wpc));
        assertEquals(0, card3.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of all different numbers in a same column.
     */
    @Test
    public void poc4Test() {
        assertEquals(POC4_SCORE, card4.calculateScore(wpc));
        assertEquals(0, card4.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of tuplas of dices with 1 and 2.
     */
    @Test
    public void poc5Test() {
        assertEquals(POC5_SCORE, card5.calculateScore(wpc));
        assertEquals(0, card5.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of tuplas of dices with 3 and 4.
     */
    @Test
    public void poc6Test() {
        assertEquals(2 * POC6_SCORE, card6.calculateScore(wpc));
        assertEquals(0, card6.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of tuplas of dices with 5 and 6.
     */
    @Test
    public void poc7Test() {
        assertEquals(2 * POC7_SCORE, card7.calculateScore(wpc));
        assertEquals(0, card7.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of groups of dices with all different numbers.
     */
    @Test
    public void poc8Test() {
        assertEquals(POC8_SCORE, card8.calculateScore(wpc));
        assertEquals(0, card8.calculateScore(wpcEmpty));
    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of dices of the same color on the same diagonal.
     */
    @Test
    public void poc9Test() {
        assertEquals(7 * POC9_SCORE, card9.calculateScore(wpc));
        assertEquals(0, card9.calculateScore(wpcEmpty));

    }

    /**
     * Tests if, given a wpc at the end of the game, the related method counts in a correct way points related to the
     * number of groups of dices with all different colors.
     */
    @Test
    public void poc10Test() {
        assertEquals(2 * POC10_SCORE, card10.calculateScore(wpc));
        assertEquals(0, card10.calculateScore(wpcEmpty));
    }
}
