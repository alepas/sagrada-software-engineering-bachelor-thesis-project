package it.polimi.ingsw.model.usersdb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHAFunction {
    private SHAFunction(){}

    static String getShaPwd(String passwordToHash, byte[] salt) throws NoSuchAlgorithmException
    {
        String pepperedPassword = passwordToHash +getPepper();
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
            e.printStackTrace();
        }
        return generatedPassword;
    }




    private static String getPepper(){

        return "�`�y�3��G�u0019��ļ�hfb8673552f4fc89b53d6a7cf9cb1c6eb�w�1U�b�Uc78ab44a9177����x�.32";
    }

}