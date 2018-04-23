package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.exceptions.passwordParsingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHAFunction {
    private SHAFunction(){}
    private static final String pepper="�`�y�3��G�u0019��ļ�hfb8673552f4fc89b53d6a7cf9cb1c6eb�w�1U�b�Uc78ab44a9177����x�.32";

    static String getShaPwd(String passwordToHash, byte[] salt) throws passwordParsingException
    {
        String pepperedPassword = passwordToHash +pepper;
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            byte[] bytes = md.digest(pepperedPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new passwordParsingException();
        }
        return generatedPassword;
    }




}