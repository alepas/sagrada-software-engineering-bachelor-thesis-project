package server.model.wpc;

import org.junit.Before;
import org.junit.Test;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import shared.clientinfo.ClientCell;
import shared.clientinfo.ClientWpc;
import shared.clientinfo.Position;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WpcTest {
    private Wpc wpc0;
    private Wpc wpc2;
    private String name;

    private ArrayList<Cell> schema = new ArrayList<>();
    private ArrayList<Cell> schema1 = new ArrayList<>();

    private Cell cell00;
    private Cell cell01;
    private Position pos01;
    private Cell cell02;
    private Cell cell03;
    private Cell cell04;

    private Cell cell10;
    private Cell cell11;
    private Cell cell12;
    private Cell cell13;
    private Cell cell14;

    private Cell cell20;
    private Cell cell21;
    private Cell cell22;
    private Cell cell23;
    private Cell cell24;

    private Cell cell30;
    private Cell cell31;
    private Cell cell32;
    private Cell cell33;
    private Cell cell34;

    private Dice dice1;
    private Dice dice2;
    private Dice dice3;
    private Dice dice4;

    @Before
    public void before(){
        dice1 = mock(Dice.class);
        when(dice1.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice1.getDiceNumber()).thenReturn(4);
        when(dice1.getId()).thenReturn(1);

        dice2 = mock(Dice.class);
        when(dice2.getDiceColor()).thenReturn(Color.VIOLET);
        when(dice2.getDiceNumber()).thenReturn(1);

        dice3 = mock(Dice.class);
        when(dice3.getDiceColor()).thenReturn(Color.GREEN);
        when(dice3.getDiceNumber()).thenReturn(5);

        dice4 = mock(Dice.class);
        when(dice4.getDiceColor()).thenReturn(Color.YELLOW);
        when(dice4.getDiceNumber()).thenReturn(4);

        int favour = 4;
        String wpcID = "1";
        wpc0 = new Wpc(wpcID, name ,favour,schema);


        cell00 = mock(Cell.class);
        Position pos00 = mock(Position.class);
        when(pos00.getRow()).thenReturn(0);
        when(pos00.getColumn()).thenReturn(0);
        when(cell00.getCellPosition()).thenReturn(pos00);
        schema1.add(cell00);


        cell01 = mock(Cell.class);
        pos01 = mock(Position.class);
        when(pos01.getRow()).thenReturn(0);
        when(pos01.getColumn()).thenReturn(1);
        when(cell01.getCellPosition()).thenReturn(pos01);
        schema1.add(cell01);


        cell02 = mock(Cell.class);
        Position pos02 = mock(Position.class);
        when(pos02.getRow()).thenReturn(0);
        when(pos02.getColumn()).thenReturn(2);
        when(cell02.getCellPosition()).thenReturn(pos02);
        schema1.add(cell02);


        cell03 = mock(Cell.class);
        Position pos03 = mock(Position.class);
        when(pos03.getRow()).thenReturn(0);
        when(pos03.getColumn()).thenReturn(3);
        when(cell03.getCellPosition()).thenReturn(pos03);
        when(cell03.getColor()).thenReturn(Color.VIOLET);
        schema1.add(cell03);

        cell04 = mock(Cell.class);
        Position pos04 = mock(Position.class);
        when(pos04.getRow()).thenReturn(0);
        when(pos04.getColumn()).thenReturn(4);
        when(cell04.getCellPosition()).thenReturn(pos04);
        when(cell04.getColor()).thenReturn(Color.GREEN);
        schema1.add(cell04);



        cell10 = mock(Cell.class);
        Position pos10 = mock(Position.class);
        when(pos10.getRow()).thenReturn(1);
        when(pos10.getColumn()).thenReturn(0);
        when(cell10.getCellPosition()).thenReturn(pos10);
        schema1.add(cell10);

        cell11 = mock(Cell.class);
        Position pos11 = mock(Position.class);
        when(pos11.getRow()).thenReturn(1);
        when(pos11.getColumn()).thenReturn(1);
        when(cell11.getCellPosition()).thenReturn(pos11);
        schema1.add(cell11);

        cell12 = mock(Cell.class);
        Position pos12 = mock(Position.class);
        when(pos12.getRow()).thenReturn(1);
        when(pos12.getColumn()).thenReturn(2);
        when(cell12.getCellPosition()).thenReturn(pos12);
        schema1.add(cell12);

        cell13 = mock(Cell.class);
        Position pos13 = mock(Position.class);
        when(pos13.getRow()).thenReturn(1);
        when(pos13.getColumn()).thenReturn(3);
        when(cell13.getCellPosition()).thenReturn(pos13);
        when(cell13.getNumber()).thenReturn(3);
        schema1.add(cell13);

        cell14 = mock(Cell.class);
        Position pos14 = mock(Position.class);
        when(pos14.getRow()).thenReturn(1);
        when(pos14.getColumn()).thenReturn(4);
        when(cell14.getCellPosition()).thenReturn(pos14);
        schema1.add(cell14);




        cell20 = mock(Cell.class);
        Position pos20 = mock(Position.class);
        when(pos20.getRow()).thenReturn(2);
        when(pos20.getColumn()).thenReturn(0);
        when(cell20.getCellPosition()).thenReturn(pos20);
        when(cell20.getNumber()).thenReturn(1);
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

        cell23 = mock(Cell.class);
        Position pos23 = mock(Position.class);
        when(pos23.getRow()).thenReturn(2);
        when(pos23.getColumn()).thenReturn(3);
        when(cell23.getCellPosition()).thenReturn(pos23);
        schema1.add(cell23);

        cell24 = mock(Cell.class);
        Position pos24 = mock(Position.class);
        when(pos24.getRow()).thenReturn(2);
        when(pos24.getColumn()).thenReturn(4);
        when(cell24.getCellPosition()).thenReturn(pos24);
        when(cell24.getNumber()).thenReturn(6);
        schema1.add(cell24);



        cell30 = mock(Cell.class);
        Position pos30 = mock(Position.class);
        when(pos30.getRow()).thenReturn(3);
        when(pos30.getColumn()).thenReturn(0);
        when(cell30.getCellPosition()).thenReturn(pos30);
        schema1.add(cell30);

        cell31 = mock(Cell.class);
        Position pos31 = mock(Position.class);
        when(pos31.getRow()).thenReturn(3);
        when(pos31.getColumn()).thenReturn(1);
        when(cell31.getCellPosition()).thenReturn(pos31);
        schema1.add(cell31);

        cell32 = mock(Cell.class);
        Position pos32 = mock(Position.class);
        when(pos32.getRow()).thenReturn(3);
        when(pos32.getColumn()).thenReturn(2);
        when(cell32.getCellPosition()).thenReturn(pos32);
        when(cell32.getNumber()).thenReturn(5);
        schema1.add(cell32);

        cell33 = mock(Cell.class);
        Position pos33 = mock(Position.class);
        when(pos33.getRow()).thenReturn(3);
        when(pos33.getColumn()).thenReturn(3);
        when(cell33.getCellPosition()).thenReturn(pos33);
        when(cell33.getColor()).thenReturn(Color.BLUE);
        schema1.add(cell33);

        cell34 = mock(Cell.class);
        Position pos34 = mock(Position.class);
        when(pos34.getRow()).thenReturn(3);
        when(pos34.getColumn()).thenReturn(4);
        when(cell34.getCellPosition()).thenReturn(pos34);
        schema1.add(cell34);

        wpc2 = new Wpc(wpcID, name, favour, schema1);
    }


    @Test
    public void wpcConstructor(){
        assertEquals("1", wpc0.getId());
        assertEquals(4, wpc0.getFavours());
        assertEquals(0, wpc0.schema.size());

        assertEquals("1", wpc2.getId());
        assertEquals(4, wpc2.getFavours());
        assertEquals(20, wpc2.schema.size());
    }


    @Test
    public void copyWpcTest(){
        Wpc wpc1 = wpc0.copyWpc();
        assertEquals(wpc1.getId(), wpc0.getId());
        assertEquals(wpc1.getFavours(), wpc0.getFavours());
        assertEquals(wpc1.schema, wpc0.schema);
    }


    /**
     *Tests if there is at least a cell where it it is possible to add a dice
     */
   @Test
   public void isDicePlaceableTest(){
        wpc2.addDiceWithAllRestrictions(dice1, cell00.getCellPosition());
        assertTrue(wpc2.isDicePlaceable(dice2));
   }

    /**
     * Tests if the addDiceWithAllRestrictions method comes true in all possible cases and false in those cases that are
     * not accepted by the game's rules.
     */
    @Test
    public void addDiceWithAllRestrictionsTest(){
        Position position = new Position(7,8);

        assertEquals(0, wpc2.getNumOfDices());
        assertEquals(20, wpc2.getNumFreeCells());
        assertTrue(wpc2.isDicePlaceable(dice1));

        assertFalse(wpc2.addDiceWithAllRestrictions(dice1, position));

        //correct initial position but different number from the cell
        assertFalse(wpc2.addDiceWithAllRestrictions(dice1, cell32.getCellPosition()));

        //incorrect initial position, cell without restrictions
        assertFalse(wpc2.addDiceWithAllRestrictions(dice1, cell13.getCellPosition()));

        //correct initial position but different color
        assertFalse(wpc2.addDiceWithAllRestrictions(dice1, cell04.getCellPosition()));

        //cell with a dice on it
//        Assert.assertFalse(wpc2.addDiceWithAllRestrictions( dice1, cell31.getCellPosition()));

        //correct cell
        assertTrue(wpc2.addDiceWithAllRestrictions(dice1, cell00.getCellPosition()));
        assertEquals(1, wpc2.getNumOfDices());
        assertEquals(19, wpc2.getNumFreeCells());

        assertFalse(wpc2.addDiceWithAllRestrictions(dice2, cell01.getCellPosition()));
        assertFalse(wpc2.addDiceWithAllRestrictions(dice4, cell01.getCellPosition()));
        assertFalse(wpc2.addDiceWithAllRestrictions(dice4, cell30.getCellPosition()));
        assertTrue(wpc2.addDiceWithAllRestrictions(dice4, cell11.getCellPosition()));
        assertEquals(2, wpc2.getNumOfDices());
        assertEquals(18, wpc2.getNumFreeCells());

        assertFalse(wpc2.addDiceWithAllRestrictions(dice3, cell11.getCellPosition()));
    }

    @Test
    public void getClientWpcTest(){
        ClientWpc clientWpc = wpc2.getClientWpc();
        assertEquals(clientWpc.getWpcID(), wpc2.getId());
        assertEquals(clientWpc.getFavours(), wpc2.getFavours());
        for(ClientCell clientCell: clientWpc.getSchema()){
            for(Cell cell: wpc2.getSchema()) {
                if(cell.getCellPosition() == clientCell.getCellPosition())
                    assertSame(cell.getCellPosition(), clientCell.getCellPosition());

            }
        }

    }

    /**
     * Checks if the relative method controls in a correct way every type of restrictions
     */
    @Test
    public void addDIcePersonalizedRestrictionsTest(){
        //shows that it is possible to add a violet dice with number 4 in a cell wihout any restriction respecting all rules
        assertFalse(wpc2.addDicePersonalizedRestrictions(dice1, cell22.getCellPosition(), true, true,
                true, true, false));

        //shows that it is not possible to add a green dice with number 5 in a violet cell when the only restriction
        // that is not checked is the numeric one
        assertFalse(wpc2.addDicePersonalizedRestrictions(dice3, cell03.getCellPosition(), true, false,
                true, true, false));

        //shows that it is possible to add a violet dice with number 1 in a violet cell when the only restriction
        //that is not checked is the color one
        assertFalse(wpc2.addDicePersonalizedRestrictions(dice1, cell13.getCellPosition(), false, true,
                true, true, false));

        //sets a dice in a correct way in a cell with a different color from the dice one
        assertTrue(wpc2.addDicePersonalizedRestrictions(dice3, cell03.getCellPosition(), false, true,
                true, true, false));

        assertFalse(wpc2.addDicePersonalizedRestrictions(dice2, cell02.getCellPosition(), true,true,
                false,false,true));

        assertFalse(wpc2.addDicePersonalizedRestrictions(dice2, cell21.getCellPosition(), true, true,
                true, true,false));

        assertTrue(wpc2.addDicePersonalizedRestrictions(dice2, cell02.getCellPosition(), true,true,
                true,true,false));
    }

    /**
     * Tests if the position is really related to a cell or not
     */
    @Test
    public void getCellFromPositionTest(){
        Position position1 = mock(Position.class);
        when(position1.getRow()).thenReturn(4);
        when(position1.getColumn()).thenReturn(2);

        Position position2 = mock(Position.class);
        when(position2.getRow()).thenReturn(6);
        when(position2.getColumn()).thenReturn(3);

        Position  position3 = mock(Position.class);
        when(position3.getRow()).thenReturn(3);
        when(position3.getColumn()).thenReturn(5);

        Position  position4 = mock(Position.class);
        when(position4.getRow()).thenReturn(0);
        when(position4.getColumn()).thenReturn(8);

        assertNull(wpc2.getCellFromPosition(position1));
        assertNull(wpc2.getCellFromPosition(position2));
        assertNull(wpc2.getCellFromPosition(position3));
        assertNull(wpc2.getCellFromPosition(position4));

        assertNotEquals(cell00, wpc2.getCellFromPosition(pos01));
    }

    /**
     * given a dice Id it checks if the method creates a dice and position with the dice related to the id
     */
    @Test
    public void getDiceFromPositionTest(){
        int Id1 = 1;
        int Id2 = 2;
        wpc2.schema.remove(cell01);
        when(cell01.getDice()).thenReturn(dice1);
        wpc2.schema.add(cell01);
        DiceAndPosition diceAndPosition = wpc2.getDiceAndPosition(Id1);
        assertEquals(dice1, diceAndPosition.getDice());
        assertEquals( pos01, diceAndPosition.getPosition() );
        assertNull(wpc2.getDiceAndPosition(Id2));

    }

    /**
     * Tests if the cell is on the border
     */
    @Test
    public void checkFirstTurnRestrictionTest(){
        assertTrue(wpc2.checkFirstTurnRestriction(cell00));
        assertTrue(wpc2.checkFirstTurnRestriction(cell04));
        assertTrue(wpc2.checkFirstTurnRestriction(cell34));
        assertTrue(wpc2.checkFirstTurnRestriction(cell30));
        assertFalse(wpc2.checkFirstTurnRestriction(cell22));
    }


    /**
     * Tests if it is possible to add the chosen dice in the chosen cell
     */
    @Test
    public void checkCellRestrictionTest(){
        //cases of respected and not respected color restriction
        assertTrue( wpc2.checkCellRestriction(cell03, dice1));
        assertFalse(wpc2.checkCellRestriction(cell03, dice3));

        assertTrue(wpc2.checkCellRestriction(cell10, dice1));

        //cases of respected and not respected number restriction
        assertFalse(wpc2.checkCellRestriction(cell20, dice1));
        assertTrue( wpc2.checkCellRestriction(cell20, dice2));

        when(cell12.getDice()).thenReturn(dice4);
        assertFalse(wpc2.checkCellRestriction(cell12, dice3));
    }

    /**
     * Tests if it is possible to add a dice in a cell with a different color, tests if number
     * restrictions are respected
     */
    @Test
    public void checkOnlyNumberCellRestrictionTest(){
        assertTrue(wpc2.checkOnlyNumberCellRestriction(cell02, dice1));
        assertTrue(wpc2.checkOnlyNumberCellRestriction(cell03, dice3));
        assertTrue(wpc2.checkOnlyNumberCellRestriction(cell20, dice2));

        assertFalse(wpc2.checkOnlyNumberCellRestriction(cell24, dice1));

        when(cell12.getDice()).thenReturn(dice4);
        assertFalse(wpc2.checkOnlyNumberCellRestriction(cell12, dice3));
    }

    /**
     * Tests if it is possible to add a dice in a cell with a different color, tests if number
     * restrictions are respected
     */
    @Test
    public void checkOnlyColorCellRestrictionTest(){
        assertTrue(wpc2.checkOnlyColorCellRestriction(cell02, dice1));
        assertTrue(wpc2.checkOnlyColorCellRestriction(cell04, dice3));
        assertTrue(wpc2.checkOnlyColorCellRestriction(cell20, dice1));

        assertFalse(wpc2.checkOnlyColorCellRestriction(cell33, dice1));

        when(cell12.getDice()).thenReturn(dice4);
        assertFalse(wpc2.checkOnlyColorCellRestriction(cell12, dice3));
    }

    /**
     * Tests if the adjacentDiceRestriction method works
     */
    @Test
    public void checkAdjacentDiceRestrictionTest(){
        ArrayList<Cell> orthoCell = new ArrayList<>();
        orthoCell.add(cell00);
        orthoCell.add(cell01);
        orthoCell.add(cell02);
        orthoCell.add(cell12);
        orthoCell.add(cell22);
        orthoCell.add(cell21);
        orthoCell.add(cell20);
        orthoCell.add(cell10);
        assertTrue(wpc2.checkAdjacentDiceRestriction(orthoCell, dice1));

        orthoCell.remove(cell12);
        when(cell12.getDice()).thenReturn(dice2);
        orthoCell.add(cell12);
        assertFalse(wpc2.checkAdjacentDiceRestriction(orthoCell, dice1));
        assertTrue(wpc2.checkAdjacentDiceRestriction(orthoCell, dice3));
    }


    /**
     * Tests if the cell is orthogonal to the position
     */
    @Test
    public void isOrthogonallyAdjacentCellTest(){
        assertTrue(wpc2.isOrthogonallyAdjacentCell(cell13,1, 2));
        assertFalse(wpc2.isOrthogonallyAdjacentCell(cell00, 1,1));
        assertFalse(wpc2.isOrthogonallyAdjacentCell(cell32, 0,0));
    }

    /**
     * Tests if the cell is diagonal to the position
     */
    @Test
    public void isDiagonallyAdjacentCellTest(){
        assertFalse(wpc2.isDiagonallyAdjacentCell(cell31, 3,2));
        assertTrue(wpc2.isDiagonallyAdjacentCell(cell14, 2,3));
    }

    /**
     * Tests if there is at least a dice near to the position chosen by the player
     */
    @Test
    public void isThereAtLeastADiceNearTest(){
        ArrayList<Cell> orthoCell = new ArrayList<>();
        when(cell12.getDice()).thenReturn(dice2);
        orthoCell.add(cell00);
        orthoCell.add(cell01);
        orthoCell.add(cell02);
        orthoCell.add(cell12);
        orthoCell.add(cell22);
        orthoCell.add(cell21);
        orthoCell.add(cell20);
        orthoCell.add(cell10);
        assertTrue(wpc2.isThereAtLeastADiceNear(orthoCell, null));
    }


    /**
     *Tests if dices on a same row or column are found in a correct way, tests if the method getWpcDices counts in
     * a correct way the number of dices on the schema
     */
    @Test
    public void getDicesTest(){
        assertEquals(0, wpc2.getRowDices(1).size());
        assertEquals(5, wpc2.getRowDicesAndEmptySpaces(1).size());
        assertEquals(0, wpc2.getColDices(1).size());
        assertEquals(0, wpc2.getNumOfDices());
        assertEquals(20, wpc2.getNumFreeCells());

        wpc2.schema.remove(cell00);
        wpc2.schema.remove(cell01);
        wpc2.schema.remove(cell02);

        when(cell00.getDice()).thenReturn(dice1);
        when(cell01.getDice()).thenReturn(dice4);
        when(cell02.getDice()).thenReturn(dice2);

        wpc2.schema.add(cell00);
        wpc2.schema.add(cell01);
        wpc2.schema.add(cell02);

        assertEquals(3, wpc2.getRowDices(0).size());
        assertEquals(5, wpc2.getRowDicesAndEmptySpaces(1).size());
        assertEquals(dice1, wpc2.getRowDices(0).get(0));

        assertEquals(0, wpc2.getRowDices(1).size());

        wpc2.schema.remove(cell20);
        wpc2.schema.remove(cell21);
        wpc2.schema.remove(cell22);
        wpc2.schema.remove(cell23);
        wpc2.schema.remove(cell33);

        when(cell20.getDice()).thenReturn(dice3);
        when(cell21.getDice()).thenReturn(dice2);
        when(cell22.getDice()).thenReturn(dice4);
        when(cell23.getDice()).thenReturn(dice1);
        when(cell24.getDice()).thenReturn(dice3);

        wpc2.schema.add(cell20);
        wpc2.schema.add(cell21);
        wpc2.schema.add(cell22);
        wpc2.schema.add(cell23);
        wpc2.schema.add(cell24);
        assertEquals(5, wpc2.getRowDices(2).size());
        assertEquals(5, wpc2.getRowDicesAndEmptySpaces(1).size());
        assertEquals(dice2, wpc2.getRowDices(2).get(1));

        assertEquals(2, wpc2.getColDices(0).size());
        assertEquals(2, wpc2.getColDices(1).size());
        assertEquals(2, wpc2.getColDices(2).size());
        assertEquals(1, wpc2.getColDices(3).size());
        assertEquals(1, wpc2.getColDices(4).size());

        assertEquals(8, wpc2.getNumOfDices());
        assertEquals(8, wpc2.getWpcDices().size());
    }


    /**
     * Tests how many dices of a certain number or color there are in the schema
     */
    @Test
    public void numDicesOfShadeAndColorTest(){
        wpc2.schema.remove(cell00);
        wpc2.schema.remove(cell01);
        wpc2.schema.remove(cell02);
        wpc2.schema.remove(cell33);

        when(cell00.getDice()).thenReturn(dice1);
        when(cell01.getDice()).thenReturn(dice4);
        when(cell02.getDice()).thenReturn(dice2);
        when(cell33.getDice()).thenReturn(dice3);

        wpc2.schema.add(cell00);
        wpc2.schema.add(cell01);
        wpc2.schema.add(cell02);
        wpc2.schema.add(cell33);

        assertEquals(1, wpc2.numDicesOfShade(1));
        assertEquals(0, wpc2.numDicesOfShade(2));
        assertEquals(0, wpc2.numDicesOfShade(3));
        assertEquals(2, wpc2.numDicesOfShade(4));
        assertEquals(1, wpc2.numDicesOfShade(5));
        assertEquals(0, wpc2.numDicesOfShade(6));

        assertEquals(2, wpc2.numDicesOfColor(Color.VIOLET));
        assertEquals(1, wpc2.numDicesOfColor(Color.GREEN));
        assertEquals(1, wpc2.numDicesOfColor(Color.YELLOW));
        assertEquals(0, wpc2.numDicesOfColor(Color.BLUE));
        assertEquals(0, wpc2.numDicesOfColor(Color.RED));
    }

    @Test
    public void autoAddDiceTest(){

    }

   /* @Test
    public void removeDiceTest(){
        for(Cell cell: wpc0.getSchema())
            wpc0.schema.remove(cell);
        when(cell01.getDice()).thenReturn(dice1);
        wpc0.schema.add(cell01);
        when(cell00.getDice()).thenReturn(dice2);
        wpc0.schema.add(cell00);
        wpc0.removeDice(cell01.getCellPosition());
        assertNull(cell01.getDice());
    }*/

}
