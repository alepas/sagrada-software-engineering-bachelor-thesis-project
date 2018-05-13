package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.DatabaseFileErrorException;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class LoadingFromFileTest{
    static private int number=764653;
    static private HashMap<String, Integer> map=new HashMap<String, Integer>();
    private final String PATH="src/test/resources/databasetest/test.db";

    @BeforeAll
    static void setUp(){
        map.put("luca",number);
        map.put("mario",number);
    }


    @Test
    void LoadWrongFile(){
        Assertions.assertThrows(DatabaseFileErrorException.class, () -> {
                    LoadingFromFile.fromFile("it/polimi/ingsw/model/userdb/testnottrue.db");

                }
        );
    }



    @Test
     void saveFile(){
        Assertions.assertDoesNotThrow(()->{
            LoadingFromFile.toFile(map,PATH);
            File f = new File(PATH);
            Assertions.assertTrue(f.isFile());
        });
    }

    @Test
    void loadFileNoExceptions(){
            Assertions.assertDoesNotThrow(()->{
            LoadingFromFile.fromFile(PATH);
        });
    }

    @Test
    void LoadRightFile() throws FileNotFoundException {
        HashMap<String, Integer> tempmap=null;
        try {
            tempmap=(HashMap<String, Integer>) LoadingFromFile.fromFile(PATH);
        } catch (DatabaseFileErrorException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(map.equals(tempmap));

/*
        Assertions.assertEquals(map.containsKey("luca"),tempmap.containsKey("luca"));
        Assertions.assertEquals(map.containsKey("mario"),tempmap.containsKey("mario"));
        map.equals()
        Assertions.assertEquals(map.values(),tempmap.values());
        Assertions.assertEquals(map.getInstance("luca"),tempmap.getInstance("luca"));
        Assertions.assertEquals(map.getInstance("mario"),tempmap.getInstance("mario"));
*/


    }
    @After
    void removeFile(){
        File f = new File(PATH);
        f.delete();
    }







}
