package it.polimi.ingsw.model.usersdb;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class DatabaseUsersTest {
    static private DatabaseUsers db1, db2, db3, db4;

    @BeforeClass
    public static void setUpClass(){
        db1 = DatabaseUsers.getInstance();
        db2 = DatabaseUsers.getInstance();
        db3 = DatabaseUsers.getInstance();
        db4 = DatabaseUsers.getInstance();
    }

    @Test
    public void getInstanceTest(){
        Assert.assertEquals(db1, db2);
        Assert.assertEquals(db2, db3);
        Assert.assertEquals(db3, db4);
    }

    @Test
    public void registerUserTest(){
        try {
            db1.registerUser("pippo", "12345");
        } catch (Exception e){

        }

    }

}
