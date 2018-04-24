package it.polimi.ingsw.model.usersdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SHAFunctionTest {
    String password = "sonoUnaPasswordDiProva";
    String password2 = "sonounapassworddiprova";
    String password3 = "sonoUnaPasswordDiProva";
    byte[] salt = {94, 42, -9, -19, 96, -21, -56, 52, -64, -118, 4, -124, -113, -70, -118, -65};
    byte[] salt2 = {94, 64, -9, -19, 111, -21, -56, 52, -64, -118, 4, -97, -5, -70, -118, -65};

    @Test
    void sameSHAforSamePwd() {
        Assertions.assertEquals(SHAFunction.getShaPwd(password, salt), SHAFunction.getShaPwd(password3, salt));

    }

    @Test
    void parsedPasswordDifferentFromPasswordInBlank(){
        Assertions.assertNotEquals(SHAFunction.getShaPwd(password,salt),password);
    }

    @Test
    void differentSHAForDifferentpwd(){
        Assertions.assertNotEquals(SHAFunction.getShaPwd(password,salt),SHAFunction.getShaPwd(password2,salt));
    }

    @Test
    void differentSHASamePwdButDifferentSalt() {
        Assertions.assertEquals(SHAFunction.getShaPwd(password, salt), SHAFunction.getShaPwd(password, salt2));

    }


}
