package it.polimi.ingsw.model.dicebagtest;

import it.polimi.ingsw.model.dicebag.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.mockito.Mockito.mock;
// non ne sono sicura
public class ColorTest {
    private Color color;
    private Random random;
    @Before
    public void Before() {
        color = Color.randomColor();
    }

    @Test
    public void checkRandomColor(){
      /*  Assert.assertTrue(()->{
            if ( color.ordinal() == 0)
                if(Color.VIOLET){
                ;
            }
        } ;*/

    }
}
