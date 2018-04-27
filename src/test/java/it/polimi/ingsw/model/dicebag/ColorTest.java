package it.polimi.ingsw.model.dicebag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.mockito.Mockito.mock;

public class ColorTest {
    private Color color;

    @Before
    public void Before() {
        color = Color.randomColor();
    }

    @Test
    public void checkRandomColor(){
        Assert.assertTrue(color == Color.VIOLET || color == Color.YELLOW || color == Color.RED || color == Color.BLUE || color == Color.GREEN);
    }

    @Test
    public void checkParseColor(){

        String violet = "violet";
        String blue = "blue";
        String green = "green";
        String yellow = "yellow";
        String red = "red";
        String noColor = "null";

        Assert.assertEquals(Color.VIOLET, Color.parseColor(violet));
        Assert.assertEquals(Color.BLUE, Color.parseColor(blue));
        Assert.assertEquals(Color.GREEN, Color.parseColor(green));
        Assert.assertEquals(Color.YELLOW, Color.parseColor(yellow));
        Assert.assertEquals(Color.RED, Color.parseColor(red));
        Assert.assertNull(Color.parseColor(noColor));
    }
}
