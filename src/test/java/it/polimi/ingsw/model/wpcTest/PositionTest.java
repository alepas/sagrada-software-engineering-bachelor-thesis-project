package it.polimi.ingsw.model.wpcTest;

import it.polimi.ingsw.model.WPC.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PositionTest {
    private Position position;
    private int row;
    private int column;

    @Before
    public void Before(){
        position = new Position(row, column);
    }

    @Test
    private void checkPositionCOnstructor(){
        Assert.assertTrue(position.getRow() >= 0 && position.getRow() <= 3);
        Assert.assertTrue(position.getColumn() >= 0 && position.getColumn() <= 4);
    }
}
