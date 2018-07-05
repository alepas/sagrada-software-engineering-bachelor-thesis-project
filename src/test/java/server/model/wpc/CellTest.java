package server.model.wpc;

import org.junit.Before;
import org.junit.Test;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import shared.clientinfo.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class CellTest {
    private Cell cell;
    private Cell cell1;
    private Dice dice;
    private Position position;
    private int cellNumber;
    private Color cellColor;

    @Before
    public void Before(){

        dice = mock(Dice.class);

        position = mock(Position.class);
        cellNumber = 1;
        cellColor = Color.VIOLET;
        cell = new Cell(position, cellColor, cellNumber);
        cell1 = new Cell(cell);
    }

    @Test
    public void checkCellConstructor(){
        assertEquals(cellNumber, cell.getNumber());
        assertEquals(cellColor, cell.getColor());
        assertEquals(position, cell.getCellPosition());
        assertNull(cell.getDice());
    }

    @Test
    public void check2ndCellConstructor(){
        assertEquals(cell.getNumber(), cell1.getNumber());
        assertEquals(cell.getColor(), cell1.getColor());
        assertEquals(cell.getCellPosition(), cell1.getCellPosition());
        assertNull(cell1.getDice());
    }

    @Test
    public void checkSetAndRemoveDice(){
        cell.setDice(dice);
        assertEquals( dice, cell.getDice());
        cell.removeDice();
        assertNull(cell.getDice());
    }
}
