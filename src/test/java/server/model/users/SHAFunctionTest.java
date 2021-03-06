package server.model.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import shared.exceptions.usersAndDatabaseExceptions.PasswordParsingException;

class SHAFunctionTest {
    private String password = "sonoUnaPasswordDiProva";
    private String password2 = "sonounapassworddiprova";
    private String password3 = "sonoUnaPasswordDiProva";
    private byte[] salt = {94, 42, -9, -19, 96, -21, -56, 52, -64, -118, 4, -124, -113, -70, -118, -65};
    private byte[] salt2 = {94, 64, -9, -19, 111, -21, -56, 52, -64, -118, 4, -97, -5, -70, -118, -65};



   @Test
   void notThrowsExceptions(){
       Assertions.assertDoesNotThrow(()-> SHAFunction.getShaPwd(password,salt));
   }

    @Test
    void sameSHAforSamePwd() throws PasswordParsingException {

        Assertions.assertEquals(SHAFunction.getShaPwd(password, salt), SHAFunction.getShaPwd(password3, salt));

    }

    @Test
    void parsedPasswordDifferentFromPasswordInBlank() throws PasswordParsingException {
        Assertions.assertNotEquals(SHAFunction.getShaPwd(password,salt),password);
    }

    @Test
    void differentSHAForDifferentpwd() throws PasswordParsingException {
        Assertions.assertNotEquals(SHAFunction.getShaPwd(password,salt),SHAFunction.getShaPwd(password2,salt));
    }

    @Test
    void differentSHASamePwdButDifferentSalt() throws PasswordParsingException {
        Assertions.assertNotEquals(SHAFunction.getShaPwd(password, salt), SHAFunction.getShaPwd(password, salt2));

    }


}
