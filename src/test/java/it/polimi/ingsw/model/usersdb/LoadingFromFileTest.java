package it.polimi.ingsw.model.usersdb;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.mock.*;

import java.io.FileNotFoundException;


public class LoadingFromFileTest {
    Object prova;

    @Test
    void LoadWrongFile(){
        Assertions.assertThrows(FileNotFoundException.class, () -> {
                    LoadingFromFile.fromFile("it/polimi/ingsw/model/userdb/testnottrue.db");
                }
        );
    }







}
