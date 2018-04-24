package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.exceptions.userExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.userExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.userExceptions.PasswordParsingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseUsers {



    private static DatabaseUsers instance ;

    private HashMap<String, User> userDataTable;

    private HashMap<String, String> tokenbyUsername;
    private HashMap<String, User> usersbyToken;


    public synchronized static DatabaseUsers getInstance(){
        if (instance==null) {
            instance = new DatabaseUsers();
            try {
                instance.userDataTable = (HashMap<String, User>) LoadingFromFile.fromFile("src/main/resources/database/store.db");

            }catch (FileNotFoundException i){
                File f = new File("src/main/resources/database/store.db");
                instance.userDataTable=new HashMap<String, User>();
            }
            instance.tokenbyUsername=new HashMap<String, String>();
            instance.usersbyToken=new HashMap<String, User>();
        }
        return instance;
    }

    private DatabaseUsers(){}

    private synchronized boolean isUsernameTaken(String username){

        return userDataTable.containsKey(username);
    }

    public synchronized String registerUser(String username, String password) throws CannotRegisterUserException{
        byte[] salt;
        String passwordHash;

        if (isUsernameTaken(username)){
            throw new CannotRegisterUserException(username,2);

        }
        System.out.println("creo nuovo utente");
            try {
                salt = getSalt();
                passwordHash = SHAFunction.getShaPwd(password, salt);
            }
            catch (PasswordParsingException e){
            throw new CannotRegisterUserException(username,1);
            }
            User newuser= new User();
            newuser.password=passwordHash;
            newuser.salt=salt;
            newuser.wonGames=0;
            newuser.lostGames=0;
            newuser.abandonedGames=0;
            newuser.ranking=0;
            userDataTable.put(username,newuser);
            updateFileDB();
            String newtoken = UUID.randomUUID().toString();
            usersbyToken.put(newtoken,newuser);
            tokenbyUsername.put(username,newtoken);
            return newtoken;


    }

    public synchronized String login(String username, String password) throws  CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        // username isn't registered
        if(!userDataTable.containsKey(username) ){
            throw new CannotLoginUserException(username,3);
        }
        User foundUser=null;
        foundUser=userDataTable.get(username);
        try {
        byte[] salt=foundUser.salt;


            passwordHash = SHAFunction.getShaPwd(password, salt);
        } catch (PasswordParsingException e) {
            throw new CannotLoginUserException(username,0);
        }
        storedPasswordHash = foundUser.password;
        if (passwordHash.equals(storedPasswordHash)){
            String newtoken = UUID.randomUUID().toString();
            if (tokenbyUsername.containsKey(username))
            {
                String oldtoken=tokenbyUsername.get(username);
                usersbyToken.remove(oldtoken);
            }

            usersbyToken.put(newtoken,foundUser);
            tokenbyUsername.put(username,newtoken);

            return newtoken;
        }
        else{
            throw new CannotLoginUserException(username,1);
        }
    }

    public synchronized String getTokoen(String user){
        return tokenbyUsername.get(user);
    }

    public synchronized void addWonGames(String token){
        if(token==null)
            System.out.println("no token : null");
        else {
            User us = usersbyToken.get(token);
            if (us == null) {
                System.out.println("no user baby");
            } else {
                us.wonGames++;
                updateFileDB();
            }
        }
    }

    public synchronized int getWonGames(String token){
        if(token==null){
            System.out.println("no token : null");
            return 0;}

        User us=usersbyToken.get(token);
        return us.wonGames;
    }


    synchronized void addWonGamesFromUsername(String user){

            User us = userDataTable.get(user);
            if (us == null) {
                System.out.println("no user baby");
            } else {
                us.wonGames++;
                updateFileDB();
            }

    }

    synchronized int getWonGamesFromUsername(String user){
        User us=userDataTable.get(user);
        return us.wonGames;
    }

    synchronized void addLostGamesFromUsername(String user){

        User us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.lostGames++;
            updateFileDB();
        }

    }
    synchronized int getLostGamesFromUsername(String user){
        User us=userDataTable.get(user);
        return us.lostGames;
    }

    synchronized void addAbandonedGamesFromUsername(String user){

        User us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.abandonedGames++;
            updateFileDB();
        }

    }

    synchronized int getAbandonedGamesFromUsername(String user){
        User us=userDataTable.get(user);
        return us.abandonedGames;
    }

    synchronized void modifyRankingFromUsername(int pointsToAdd, String user){

        User us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.ranking+=pointsToAdd;
            updateFileDB();
        }

    }
    synchronized int getRankingFromUsername(String user){
        User us=userDataTable.get(user);
        return us.ranking;
    }


    static byte[] getSalt() throws PasswordParsingException {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        catch(NoSuchAlgorithmException e) {
            throw new PasswordParsingException();
        }
                    byte[] salt = new byte[16];
            sr.nextBytes(salt);

        return salt;
    }

    private void updateFileDB(){
        LoadingFromFile.toFile(userDataTable, "src/main/resources/database/store.db");
    }


}
