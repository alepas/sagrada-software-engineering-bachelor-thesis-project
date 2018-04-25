package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.exceptions.userExceptions.CannotFindUserInDB;
import it.polimi.ingsw.model.exceptions.userExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.userExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.userExceptions.NullTokenException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DatabaseUsersAttributeTest {
    static private DatabaseUsers db1, db2, db3, db4;
    static String path= "src/test/resources/databasetest/database.db";
    static File f;



    @BeforeAll
    public static void setUpClass(){
        db1 = DatabaseUsers.getInstance(path);
        db2 = DatabaseUsers.getInstance(path);
        db3 = DatabaseUsers.getInstance(path);
        db4 = DatabaseUsers.getInstance(path);
        f=new File(path);
        if (f.isFile())
            f.delete();
    }

    @Test
    public void getInstanceTest(){
        Assertions.assertEquals(db1, db2);
        Assertions.assertEquals(db2, db3);
        Assertions.assertEquals(db3, db4);
    }

    @Test
    public void registerUserTest(){
        Assertions.assertDoesNotThrow(()->{
            db1.registerUser("pippo", "12345");
        });
    }

    @Test
    public void registerTheSameUser(){
        db1.registerUser("affo", "12345");
        Assertions.assertThrows(CannotRegisterUserException.class,()->{db1.registerUser("affo", "12345");});
    }

    @Test
    public void loginRegisteredUser(){
        db1.registerUser("baffo", "12345");
        Assertions.assertDoesNotThrow(()->{db1.login("baffo", "12345");});
    }

    @Test
    void loginWrongPasswordUser(){
        db1.registerUser("becco", "2907");
        Assertions.assertThrows(CannotLoginUserException.class,()->{db1.login("becco", "12345");});

    }


    @Test
    void loginWrongUser(){
        Assertions.assertThrows(CannotLoginUserException.class,()->{db1.login("bababa", "12345");});
    }


    @Test
    void differentTokensForDifferentUsers(){
        Assertions.assertNotEquals(db1.login("affo","12345"),db1.login("baffo","12345"));
    }

    @Test
    void differentTokensSameUserMoreLogins(){
        Assertions.assertNotEquals(db1.login("affo","12345"),db1.login("affo","12345"));
    }

    @Test
    void getWonGamesFromWrongToken(){
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getWonGamesFromToken("WrongToken123");});
    }

    @Test
    void getWonGamesFromNullToken(){
        Assertions.assertThrows(NullTokenException.class,()->{db1.getWonGamesFromToken(null);});
    }

    @Test
    void getWonGamesFromRightToken(){
        String token=db1.login("affo","12345");
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getWonGamesFromToken(token);});
    }



    @Test
    void getLostGamesFromWrongToken(){
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getLostGamesFromToken("WrongToken123");});
    }

    @Test
    void getLostGamesFromNullToken(){
        Assertions.assertThrows(NullTokenException.class,()->{db1.getLostGamesFromToken(null);});
    }

    @Test
    void getLostGamesFromRightToken(){
        String token=db1.login("affo","12345");
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getLostGamesFromToken(token);});
    }


    @Test
    void getAbandonedGamesFromWrongToken(){
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getAbandonedGamesFromToken("WrongToken123");});
    }

    @Test
    void getAbandonedGamesFromNullToken(){
        Assertions.assertThrows(NullTokenException.class,()->{db1.getAbandonedGamesFromToken(null);});
    }

    @Test
    void getAbandonedGamesFromRightToken(){
        String token=db1.login("affo","12345");
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getAbandonedGamesFromToken(token);});
    }


    @Test
    void getRankingFromWrongToken(){
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getRankingFromToken("WrongToken123");});
    }

    @Test
    void getRankingFromNullToken(){
        Assertions.assertThrows(NullTokenException.class,()->{db1.getRankingFromToken(null);});
    }

    @Test
    void getRankingFromRightToken(){
        String token=db1.login("affo","12345");
        Assertions.assertThrows(CannotFindUserInDB.class,()->{db1.getRankingFromToken(token);});
    }




    @AfterAll
    static void removeFile(){
        if (f!=null)
            f.delete();
    }
}


