package it.polimi.ingsw.model.usersdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SHAFunctionTest {
    String password="sonoUnaPasswordDiProva";
    String password2="sonounapassworddiprova";
    String password3="sonoUnaPasswordDiProva";
    Byte[] salt={0x1,0x2,0x3,0x45,0x6,0x7,0x13,0x15,0x12,0x40,0x43,0x71,0x19,0x52,0x61,0x8};

    @Test
    void sameSHAforSamePwd() {
        Assertions.assertEquals(SHAFunction.getShaPwd(password, salt), );


        assertEquals(2, 2);
        assertEquals(4, 4, "The optional assertion message is now the last parameter.");
    }