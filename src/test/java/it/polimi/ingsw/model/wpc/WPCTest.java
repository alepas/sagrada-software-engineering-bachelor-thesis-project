package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WPCTest {
    private WPC wpc0;
    private WPC wpc2;

    private ArrayList<Cell> schema = new ArrayList<>();
    private ArrayList<Cell> schema1 = new ArrayList<>();

    private Cell cell01;
    private Cell cell21;
    private Cell cell22;
    private Cell cell32;

    private Dice dice1;

    @Before
    public void before(){
        int favour = 4;
        String wpcID = "1";
        wpc0 = new WPC(wpcID, favour,schema);


        Cell cell00 = mock(Cell.class);
        Position pos00 = mock(Position.class);
        when(pos00.getRow()).thenReturn(0);
        when(pos00.getColumn()).thenReturn(0);
        when(cell00.getCellPosition()).thenReturn(pos00);
        schema1.add(cell00);

        dice1 = mock(Dice.class);
        when(dice1.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice1.getDiceNumber()).thenReturn(4);
        cell01 = mock(Cell.class);
        Position pos01 = mock(Position.class);
        when(pos01.getRow()).thenReturn(0);
        when(pos01.getColumn()).thenReturn(1);
        when(cell01.getCellPosition()).thenReturn(pos01);
        when(cell01.getDice()).thenReturn(dice1);
        schema1.add(cell01);

        Dice dice2 = mock(Dice.class);
        when(dice2.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice2.getDiceNumber()).thenReturn(1);
        Cell cell02 = mock(Cell.class);
        Position pos02 = mock(Position.class);
        when(pos02.getRow()).thenReturn(0);
        when(pos02.getColumn()).thenReturn(2);
        when(cell02.getCellPosition()).thenReturn(pos02);
        when(cell02.getDice()).thenReturn(dice2);
        schema1.add(cell02);

        Dice dice3 = mock(Dice.class);
        when(dice3.getDiceColor()).thenReturn(Color.GREEN);
        when(dice3.getDiceNumber()).thenReturn(5);
        Cell cell03 = mock(Cell.class);
        Position pos03 = mock(Position.class);
        when(pos03.getRow()).thenReturn(0);
        when(pos03.getColumn()).thenReturn(3);
        when(cell03.getCellPosition()).thenReturn(pos03);
        when(cell03.getDice()).thenReturn(dice3);
        schema1.add(cell03);

        Cell cell04 = mock(Cell.class);
        Position pos04 = mock(Position.class);
        when(pos04.getRow()).thenReturn(0);
        when(pos04.getColumn()).thenReturn(4);
        when(cell04.getCellPosition()).thenReturn(pos04);
        when(cell04.getColor()).thenReturn(Color.GREEN);
        schema1.add(cell04);




        Cell cell10 = mock(Cell.class);
        Position pos10 = mock(Position.class);
        when(pos10.getRow()).thenReturn(1);
        when(pos10.getColumn()).thenReturn(0);
        when(cell10.getCellPosition()).thenReturn(pos10);
        schema1.add(cell10);

        Cell cell11 = mock(Cell.class);
        Position pos11 = mock(Position.class);
        when(pos11.getRow()).thenReturn(1);
        when(pos11.getColumn()).thenReturn(1);
        when(cell11.getCellPosition()).thenReturn(pos11);
        schema1.add(cell11);

        Cell cell12 = mock(Cell.class);
        Position pos12 = mock(Position.class);
        when(pos12.getRow()).thenReturn(1);
        when(pos12.getColumn()).thenReturn(2);
        when(cell12.getCellPosition()).thenReturn(pos12);
        schema1.add(cell12);

        Cell cell13 = mock(Cell.class);
        Position pos13 = mock(Position.class);
        when(pos13.getRow()).thenReturn(1);
        when(pos13.getColumn()).thenReturn(3);
        when(cell13.getCellPosition()).thenReturn(pos13);
        schema1.add(cell13);

        Cell cell14 = mock(Cell.class);
        Position pos14 = mock(Position.class);
        when(pos14.getRow()).thenReturn(1);
        when(pos14.getColumn()).thenReturn(4);
        when(cell14.getCellPosition()).thenReturn(pos14);
        schema1.add(cell14);




        Cell cell20 = mock(Cell.class);
        Position pos20 = mock(Position.class);
        when(pos20.getRow()).thenReturn(2);
        when(pos20.getColumn()).thenReturn(0);
        when(cell20.getCellPosition()).thenReturn(pos20);
        schema1.add(cell20);

        cell21 = mock(Cell.class);
        Position pos21 = mock(Position.class);
        when(pos21.getRow()).thenReturn(2);
        when(pos21.getColumn()).thenReturn(1);
        when(cell21.getCellPosition()).thenReturn(pos21);
        schema1.add(cell21);

        cell22 = mock(Cell.class);
        Position pos22 = mock(Position.class);
        when(pos22.getRow()).thenReturn(2);
        when(pos22.getColumn()).thenReturn(2);
        when(cell22.getCellPosition()).thenReturn(pos22);
        when(cell22.getColor()).thenReturn(Color.BLUE);
        schema1.add(cell22);

        Cell cell23 = mock(Cell.class);
        Position pos23 = mock(Position.class);
        when(pos23.getRow()).thenReturn(2);
        when(pos23.getColumn()).thenReturn(3);
        when(cell23.getCellPosition()).thenReturn(pos23);
        schema1.add(cell23);

        Cell cell24 = mock(Cell.class);
        Position pos24 = mock(Position.class);
        when(pos24.getRow()).thenReturn(2);
        when(pos24.getColumn()).thenReturn(4);
        when(cell24.getCellPosition()).thenReturn(pos24);
        when(cell24.getNumber()).thenReturn(6);
        schema1.add(cell24);




        Dice dice15 = mock(Dice.class);
        when(dice15.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice15.getDiceNumber()).thenReturn(4);
        Cell cell30 = mock(Cell.class);
        Position pos30 = mock(Position.class);
        when(pos30.getRow()).thenReturn(3);
        when(pos30.getColumn()).thenReturn(0);
        when(cell30.getCellPosition()).thenReturn(pos30);
        when(cell30.getDice()).thenReturn(dice15);
        schema1.add(cell30);

        Dice dice16 = mock(Dice.class);
        when(dice16.getDiceColor()).thenReturn(Color.RED);
        when(dice16.getDiceNumber()).thenReturn(2);
        Cell cell31 = mock(Cell.class);
        Position pos31 = mock(Position.class);
        when(pos31.getRow()).thenReturn(3);
        when(pos31.getColumn()).thenReturn(1);
        when(cell31.getCellPosition()).thenReturn(pos31);
        when(cell31.getDice()).thenReturn(dice16);
        schema1.add(cell31);

        cell32 = mock(Cell.class);
        Position pos32 = mock(Position.class);
        when(pos32.getRow()).thenReturn(3);
        when(pos32.getColumn()).thenReturn(2);
        when(cell32.getCellPosition()).thenReturn(pos32);
        schema1.add(cell32);

        Cell cell33 = mock(Cell.class);
        Position pos33 = mock(Position.class);
        when(pos33.getRow()).thenReturn(3);
        when(pos33.getColumn()).thenReturn(3);
        when(cell33.getCellPosition()).thenReturn(pos33);
        when(cell33.getColor()).thenReturn(Color.BLUE);
        schema1.add(cell33);

        Cell cell34 = mock(Cell.class);
        Position pos34 = mock(Position.class);
        when(pos34.getRow()).thenReturn(3);
        when(pos34.getColumn()).thenReturn(4);
        when(cell34.getCellPosition()).thenReturn(pos34);
        schema1.add(cell34);

        wpc2 = new WPC(wpcID, favour, schema1);
    }


    @Test
    public void wpcConstructor(){
        Assert.assertEquals("1", wpc0.getId());
        Assert.assertEquals(4, wpc0.getFavours());
        Assert.assertEquals(0, wpc0.schema.size());

        Assert.assertEquals("1", wpc2.getId());
        Assert.assertEquals(4, wpc2.getFavours());
        Assert.assertEquals(20, wpc2.schema.size());
    }


    @Test
    public void copyWpcTest(){
        WPC wpc1 = wpc0.copyWpc();
        Assert.assertEquals(wpc1.getId(), wpc0.getId());
        Assert.assertEquals(wpc1.getFavours(), wpc0.getFavours());
        Assert.assertEquals(wpc1.schema, wpc0.schema);
    }


   /* @Test
    public void insertionOfADiceTest() {
        Dice dice0 = mock(Dice.class);
        when(dice0.getDiceColor()).thenReturn(Color.RED);
        when(dice0.getDiceNumber()).thenReturn(3);
        int turn1 = 1;
        int turn2 = 3;
        Assert.assertFalse(wpc2.addDiceWithAllRestrictions(dice0, cell22, turn1));
        Assert.assertFalse(wpc2.addDiceWithAllRestrictions(dice0, cell22, turn2));
        Assert.assertTrue(wpc2.addDiceWithAllRestrictions(dice0, cell21, turn2));

    }

*/
    @Test
    public void columnDicesTest(){
        for(Cell cell: schema1)
            System.out.println(cell.getDice());
        Assert.assertEquals(0, wpc2.getColDices(0).size());
        Assert.assertEquals(0, wpc2.getColDices(1).size());
        Assert.assertEquals(0, wpc2.getColDices(2).size());
        Assert.assertEquals(0, wpc2.getColDices(3).size());
        Assert.assertEquals(0, wpc2.getColDices(4).size());
    }

    @Test
    public void rowDicesTest(){
        Assert.assertEquals(0, wpc2.getRowDices(1).size());
    }

    @Test
    public void wpcDicesTest(){
        Assert.assertEquals(0, wpc0.getWpcDices().size());
        Assert.assertEquals(0, wpc2.getWpcDices().size());
    }


    @Test
    public void numDicesOfShadeTest(){
        Assert.assertEquals(0, wpc2.numDicesOfShade(1));
        Assert.assertEquals(0, wpc2.numDicesOfShade(2));
        Assert.assertEquals(0, wpc2.numDicesOfShade(3));
        Assert.assertEquals(0, wpc2.numDicesOfShade(4));
        Assert.assertEquals(0, wpc2.numDicesOfShade(5));
        Assert.assertEquals(0, wpc2.numDicesOfShade(6));
    }

    @Test
    public void numDicesOfColorTest(){
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.BLUE));
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.VIOLET));
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.RED));
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.YELLOW));
        Assert.assertEquals(0, wpc2.numDicesOfColor(Color.GREEN));
    }

    @Test
    public void FreeCellsTest(){
        Assert.assertEquals(20, wpc2.getNumFreeCells());
    }

    @Test
    public void scorePrivateObjectiveTest(){
        Assert.assertEquals(0,  wpc2.countTotalPrivateObjective(Color.BLUE));
        Assert.assertEquals(0, wpc2.countTotalPrivateObjective(Color.VIOLET));
        Assert.assertEquals(0,  wpc2.countTotalPrivateObjective(Color.RED));
        Assert.assertEquals(0,  wpc2.countTotalPrivateObjective(Color.YELLOW));
        Assert.assertEquals(0, wpc2.countTotalPrivateObjective(Color.GREEN));
    }
}
