package it.polimi.ingsw.model;

import org.junit.Before;

import static org.mockito.Mockito.mock;

public class DiceTest {
    private Dice dice;
    private Color color;

    @Before
    public void before() {
        color = mock();

        dice = new Dice(color);
    }
}