package it.polimi.ingsw.model.usersdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    String pwd = "Fragole";
    byte[] salt = {94, 64, -9, -19, 111, -21, -56, 52, -64, -118, 4, -97, -5, -70, -118, -65};
    User mio=null;

    @BeforeEach
    void setUp(){
        mio=new User("username", pwd,salt);
    }

    @Test
    void userConstructorTest(){

        Assertions.assertEquals(pwd,mio.getPassword());
        Assertions.assertEquals(salt,mio.getSalt());
        Assertions.assertEquals(0,mio.getWonGames());
        Assertions.assertEquals(0,mio.getLostGames());
        Assertions.assertEquals(0,mio.getAbandonedGames());
        Assertions.assertEquals(0,mio.getRanking());
    }

    @Test
    void addPointsToRaking(){
        mio.addPointsToRanking(10);
        Assertions.assertEquals(10,mio.getRanking());
        mio.addPointsToRanking(-3);
        Assertions.assertEquals(7,mio.getRanking());

    }

    @Test
    void addWonGames(){
        mio.addWonGames();
        Assertions.assertEquals(1,mio.getWonGames());
    }



    @Test
    void addLostGames(){
        mio.addLostGames();
        Assertions.assertEquals(1,mio.getLostGames());
    }


    @Test
    void addAbandonedGames(){
        mio.addAbandonedGames();
        Assertions.assertEquals(1,mio.getAbandonedGames());
    }

    @Test
    void addPointsToRanking(){
        mio.addPointsToRanking(10);
        Assertions.assertEquals(10,mio.getRanking());
    }

}
