package it.polimi.ingsw.model.usersdb;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.mock.*;

import java.io.File;
import java.io.FileNotFoundException;


public class LoadingFromFileTest{

    @Test
    void LoadWrongFile(){
        Assertions.assertThrows(FileNotFoundException.class, () -> {
                    LoadingFromFile.fromFile("it/polimi/ingsw/model/userdb/testnottrue.db");
                }
        );
    }

    @Test
    void LoadRightFile(){
        Assertions.assertDoesNotThrow(()->{
            LoadingFromFile.fromFile("it/polimi/ingsw/model/userdb/test.db");
        });
    }

    @Test
     void saveFile(){
        Assertions.assertDoesNotThrow(()->{
            String teststring="bababa";
            LoadingFromFile.toFile(teststring,"it/polimi/ingsw/model/userdb/filesaved.file");
            File f = new File("it/polimi/ingsw/model/userdb/filesaved.file");
            Assertions.assertTrue(f.isFile());
        });
    }







}
