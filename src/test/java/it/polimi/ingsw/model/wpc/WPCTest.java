package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import it.polimi.ingsw.model.exceptions.wpcExceptions.NotExistingCellException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WPCTest {
    private WPC wpc0;
    private WPC wpc1;
    private WPC wpc2;

    private int favour = 4;
    private String wpcID = "1";
    private ArrayList<Cell> schema = new ArrayList<>();

    private Cell cell00;
    private Position pos00;
    private Cell cell01;
    private Position pos01;
    private Dice dice1;
    private Cell cell02;
    private Position pos02;
    private Cell cell03;
    private Position pos03;
    private Cell cell04;
    private Position pos04;

    private Cell cell10;
    private Position pos10;
    private Cell cell11;
    private Position pos11;
    private Cell cell12;
    private Position pos12;
    private Cell cell13;
    private Position pos13;
    private Cell cell14;
    private Position pos14;

    private Cell cell20;
    private Position pos20;
    private Cell cell21;
    private Position pos21;
    private Cell cell22;
    private Position pos22;
    private Cell cell23;
    private Position pos23;
    private Cell cell24;
    private Position pos24;

    private Cell cell30;
    private Position pos30;
    private Cell cell31;
    private Position pos31;
    private Cell cell32;
    private Position pos32;
    private Cell cell33;
    private Position pos33;
    private Cell cell34;
    private Position pos34;

    @Before
    public void before(){
        wpc0 = new WPC(wpcID,favour,schema);
        wpc2 = new WPC(wpcID, favour, schema);


        cell00 = mock(Cell.class);
        pos00 = mock(Position.class);
        when(pos00.getRow()).thenReturn(0);
        when(pos00.getColumn()).thenReturn(0);
        when(cell00.getCellPosition()).thenReturn(pos00);
        wpc2.schema.add(cell00);

        dice1 = mock(Dice.class);
        when(dice1.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice1.getDiceNumber()).thenReturn(4);
        cell01 = mock(Cell.class);
        pos01 = mock(Position.class);
        when(pos01.getRow()).thenReturn(0);
        when(pos01.getColumn()).thenReturn(1);
        when(cell01.getCellPosition()).thenReturn(pos01);
        when(cell01.getCellDice()).thenReturn(dice1);
        wpc2.schema.add(cell01);

        Dice dice2 = mock(Dice.class);
        when(dice2.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice2.getDiceNumber()).thenReturn(1);
        cell02 = mock(Cell.class);
        pos02 = mock(Position.class);
        when(pos02.getRow()).thenReturn(0);
        when(pos02.getColumn()).thenReturn(2);
        when(cell02.getCellPosition()).thenReturn(pos02);
        when(cell02.getCellDice()).thenReturn(dice2);
        wpc2.schema.add(cell02);

        Dice dice3 = mock(Dice.class);
        when(dice3.getDiceColor()).thenReturn(Color.GREEN);
        when(dice3.getDiceNumber()).thenReturn(5);
        cell03 = mock(Cell.class);
        pos03 = mock(Position.class);
        when(pos03.getRow()).thenReturn(0);
        when(pos03.getColumn()).thenReturn(3);
        when(cell03.getCellPosition()).thenReturn(pos03);
        when(cell03.getCellDice()).thenReturn(dice3);
        wpc2.schema.add(cell03);

        cell04 = mock(Cell.class);
        pos04 = mock(Position.class);
        when(pos04.getRow()).thenReturn(0);
        when(pos04.getColumn()).thenReturn(4);
        when(cell04.getCellPosition()).thenReturn(pos04);
        when(cell04.getCellColor()).thenReturn(Color.GREEN);
        wpc2.schema.add(cell04);




        /*Dice dice5 = mock(Dice.class);
        when(dice5.getDiceColor()).thenReturn(Color.GREEN);
        when(dice5.getDiceNumber()).thenReturn(5);*/
        cell10 = mock(Cell.class);
        pos10 = mock(Position.class);
        when(pos10.getRow()).thenReturn(1);
        when(pos10.getColumn()).thenReturn(0);
        when(cell10.getCellPosition()).thenReturn(pos10);
        wpc2.schema.add(cell10);

        /*Dice dice6 = mock(Dice.class);
        when(dice6.getDiceColor()).thenReturn(Color.RED);
        when(dice6.getDiceNumber()).thenReturn(3);*/
        cell11 = mock(Cell.class);
        pos11 = mock(Position.class);
        when(pos11.getRow()).thenReturn(1);
        when(pos11.getColumn()).thenReturn(1);
        when(cell11.getCellPosition()).thenReturn(pos11);
        //when(cell11.getCellDice()).thenReturn(dice6);
        wpc2.schema.add(cell11);

        /*Dice dice7 = mock(Dice.class);
        when(dice7.getDiceColor()).thenReturn(Color.GREEN);
        when(dice7.getDiceNumber()).thenReturn(4);*/
        cell12 = mock(Cell.class);
        pos12 = mock(Position.class);
        when(pos12.getRow()).thenReturn(1);
        when(pos12.getColumn()).thenReturn(2);
        when(cell12.getCellPosition()).thenReturn(pos12);
        //when(cell12.getCellDice()).thenReturn(dice7);
        wpc2.schema.add(cell12);

        /*Dice dice8 = mock(Dice.class);
        when(dice8.getDiceColor()).thenReturn(Color.RED);
        when(dice8.getDiceNumber()).thenReturn(2);*/
        cell13 = mock(Cell.class);
        pos13 = mock(Position.class);
        when(pos13.getRow()).thenReturn(1);
        when(pos13.getColumn()).thenReturn(3);
        when(cell10.getCellPosition()).thenReturn(pos13);
        //when(cell13.getCellDice()).thenReturn(dice8);
        wpc2.schema.add(cell13);

        cell14 = mock(Cell.class);
        pos14 = mock(Position.class);
        when(pos14.getRow()).thenReturn(1);
        when(pos14.getColumn()).thenReturn(4);
        when(cell14.getCellPosition()).thenReturn(pos14);
        wpc2.schema.add(cell14);




        /*Dice dice10 = mock(Dice.class);
        when(dice10.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice10.getDiceNumber()).thenReturn(1);*/
        cell20 = mock(Cell.class);
        pos20 = mock(Position.class);
        when(pos20.getRow()).thenReturn(2);
        when(pos20.getColumn()).thenReturn(0);
        when(cell20.getCellPosition()).thenReturn(pos20);
        //when(cell20.getCellDice()).thenReturn(dice10);
        wpc2.schema.add(cell20);

        /*Dice dice11 = mock(Dice.class);
        when(dice11.getDiceColor()).thenReturn(Color.GREEN);
        when(dice11.getDiceNumber()).thenReturn(4);*/
        cell21 = mock(Cell.class);
        pos21 = mock(Position.class);
        when(pos21.getRow()).thenReturn(2);
        when(pos21.getColumn()).thenReturn(1);
        when(cell21.getCellPosition()).thenReturn(pos21);
        //when(cell21.getCellDice()).thenReturn(dice11);
        wpc2.schema.add(cell21);

        cell22 = mock(Cell.class);
        pos22 = mock(Position.class);
        when(pos22.getRow()).thenReturn(2);
        when(pos22.getColumn()).thenReturn(2);
        when(cell22.getCellPosition()).thenReturn(pos22);
        when(cell22.getCellColor()).thenReturn(Color.BLUE);
        wpc2.schema.add(cell22);

        /*Dice dice13 = mock(Dice.class);
        when(dice13.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice13.getDiceNumber()).thenReturn(1);*/
        cell23 = mock(Cell.class);
        pos23 = mock(Position.class);
        when(pos23.getRow()).thenReturn(2);
        when(pos23.getColumn()).thenReturn(3);
        when(cell23.getCellPosition()).thenReturn(pos23);
        //when(cell23.getCellDice()).thenReturn(dice13);
        wpc2.schema.add(cell23);


        cell24 = mock(Cell.class);
        pos24 = mock(Position.class);
        when(pos24.getRow()).thenReturn(2);
        when(pos24.getColumn()).thenReturn(4);
        when(cell24.getCellPosition()).thenReturn(pos24);
        when(cell24.getCellNumber()).thenReturn(6);
        wpc2.schema.add(cell24);




        Dice dice15 = mock(Dice.class);
        when(dice15.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice15.getDiceNumber()).thenReturn(4);
        cell30 = mock(Cell.class);
        pos30 = mock(Position.class);
        when(pos30.getRow()).thenReturn(3);
        when(pos30.getColumn()).thenReturn(0);
        when(cell30.getCellPosition()).thenReturn(pos30);
        when(cell30.getCellDice()).thenReturn(dice15);
        wpc2.schema.add(cell30);

        Dice dice16 = mock(Dice.class);
        when(dice16.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice16.getDiceNumber()).thenReturn(2);
        cell31 = mock(Cell.class);
        pos31 = mock(Position.class);
        when(pos31.getRow()).thenReturn(3);
        when(pos31.getColumn()).thenReturn(1);
        when(cell31.getCellPosition()).thenReturn(pos31);
        when(cell31.getCellDice()).thenReturn(dice16);
        wpc2.schema.add(cell31);

        /*Dice dice17 = mock(Dice.class);
        when(dice17.getDiceColor()).thenReturn(Color.RED);
        when(dice17.getDiceNumber()).thenReturn(3);*/
        cell32 = mock(Cell.class);
        pos32 = mock(Position.class);
        when(pos32.getRow()).thenReturn(3);
        when(pos32.getColumn()).thenReturn(2);
        when(cell32.getCellPosition()).thenReturn(pos32);
        //when(cell32.getCellDice()).thenReturn(dice17);
        wpc2.schema.add(cell32);

        cell33 = mock(Cell.class);
        pos33 = mock(Position.class);
        when(pos33.getRow()).thenReturn(3);
        when(pos33.getColumn()).thenReturn(3);
        when(cell30.getCellPosition()).thenReturn(pos33);
        when(cell33.getCellColor()).thenReturn(Color.BLUE);
        wpc2.schema.add(cell33);

        cell34 = mock(Cell.class);
        pos34 = mock(Position.class);
        when(pos34.getRow()).thenReturn(3);
        when(pos34.getColumn()).thenReturn(4);
        when(cell30.getCellPosition()).thenReturn(pos34);
        wpc2.schema.add(cell34);
    }


    @Test
    public void wpcConstructor(){
        Assert.assertEquals("1", wpc0.getWpcID());
        Assert.assertEquals(4, wpc0.getFavours());
        //Assert.assertEquals(20, wpc0.schema.size());
    }


    @Test
    public void copyWpcTest(){
        wpc1 = wpc0.copyWpc();
        Assert.assertEquals(wpc1.getWpcID(), wpc0.getWpcID());
        Assert.assertEquals(wpc1.getFavours(), wpc0.getFavours());
        Assert.assertEquals(wpc1.schema, wpc0.schema);
    }


    @Test
    public void insertionOfADiceTest() {

        Dice dice0 = mock(Dice.class);
        when(dice0.getDiceColor()).thenReturn(Color.RED);
        when(dice0.getDiceNumber()).thenReturn(3);
        int turn1 = 1;
        int turn2 = 3;
        Assert.assertFalse(wpc2.addDice(dice0, cell22, turn1));
        Assert.assertFalse(wpc2.addDice(dice0, cell22, turn2));
        Assert.assertTrue(wpc2.addDice(dice0, cell21, turn2));
        //Assert.assertEquals(1, wpc2.getWpcDices().size());
    }


    @Test
    public void columnDicesTest(){
        Assert.assertEquals(1, wpc2.getColDices(0).size());
        Assert.assertEquals(2, wpc2.getColDices(1).size());
        Assert.assertEquals(1, wpc2.getColDices(2).size());
        Assert.assertEquals(1, wpc2.getColDices(3).size());
        Assert.assertEquals(0, wpc2.getColDices(4).size());
    }

    @Test
    public void rowDicesTest(){
        Assert.assertEquals(0, wpc2.getRowDices(1).size());
    }

    @Test
    public void wpcDicesTest(){
        Assert.assertEquals(0, wpc0.getWpcDices().size());
        Assert.assertEquals(5, wpc2.getWpcDices().size());
    }


    @Test
    public void numDicesOfShadeTest(){
        Assert.assertEquals(1, wpc2.numDicesOfShade(1));
        Assert.assertEquals(1, wpc2.numDicesOfShade(2));
        Assert.assertEquals(0, wpc2.numDicesOfShade(3));
        Assert.assertEquals(2, wpc2.numDicesOfShade(4));
        Assert.assertEquals(1, wpc2.numDicesOfShade(5));
        Assert.assertEquals(0, wpc2.numDicesOfShade(6));
    }

    @Test
    public void numDicesOfColorTest(){
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.BLUE));
        Assert.assertEquals(2, wpc2.numDicesOfColor(Color.VIOLET));
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.RED));
        Assert.assertEquals(2, wpc2.numDicesOfColor(Color.YELLOW));
        Assert.assertEquals(1, wpc2.numDicesOfColor(Color.GREEN));
    }

    @Test
    public void FreeCellsTest(){
        Assert.assertEquals(15, wpc2.getNumFreeCells());
    }

    @Test
    public void scorePrivateObjectiveTest(){
        Assert.assertEquals(0,  wpc2.countTotalPrivateObjective(Color.BLUE));
        Assert.assertEquals(6, wpc2.countTotalPrivateObjective(Color.VIOLET));
        Assert.assertEquals(0,  wpc2.countTotalPrivateObjective(Color.RED));
        Assert.assertEquals(5,  wpc2.countTotalPrivateObjective(Color.YELLOW));
        Assert.assertEquals(5, wpc2.countTotalPrivateObjective(Color.GREEN));
    }
}
