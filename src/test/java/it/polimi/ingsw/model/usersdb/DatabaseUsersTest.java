package it.polimi.ingsw.model.usersdb;



import it.polimi.ingsw.model.exceptions.userExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.userExceptions.NoUserInDBException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseUsersTest {
    static private DatabaseUsers db1, db2, db3, db4;

    @BeforeAll
    public static void setUpClass(){
        db1 = DatabaseUsers.getInstance();
        db2 = DatabaseUsers.getInstance();
        db3 = DatabaseUsers.getInstance();
        db4 = DatabaseUsers.getInstance();
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

    }


