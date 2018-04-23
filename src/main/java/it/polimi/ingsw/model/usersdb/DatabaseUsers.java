package it.polimi.ingsw.model.usersdb;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseUsers {



    private static DatabaseUsers instance ;

    private HashMap<String, UserInDB> userDataTable;

    private HashMap<String, String> tokenbyUsername;
    private HashMap<String, UserInDB> usersbyToken;


    public synchronized static DatabaseUsers getInstance(){
        if (instance==null) {
            instance = new DatabaseUsers();
            try {
                instance.userDataTable = (HashMap<String, UserInDB>) UserLoadingFromFile.fromFile("src/main/resources/database/store.db");

            }catch (FileNotFoundException i){
                File f = new File("src/main/resources/database/store.db");
                instance.userDataTable=new HashMap<String, UserInDB>();
            }
            instance.tokenbyUsername=new HashMap<String, String>();
            instance.usersbyToken=new HashMap<String, UserInDB>();
        }
        return instance;
    }

    private DatabaseUsers(){}

    private synchronized boolean isUsernameTaken(String username){
        return userDataTable.containsKey(username);
    }

    public synchronized String registerUser(String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (isUsernameTaken(username)){
            System.out.println("nickname non valido");
            return null;
        }
        else {
            System.out.println("creonuovoutente");
            byte[] salt = getSalt();
            String passwordHash = SHAFunction.getShaPwd(password,salt);
            UserInDB newuser= new UserInDB();
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
    }

    public synchronized String login(String username, String password) throws NoSuchAlgorithmException, IOException{

        // username isn't registered
        if(!userDataTable.containsKey(username) ){
            System.out.println("login scorretto");
            return null;
        }
        UserInDB foundUser=null;
        foundUser=userDataTable.get(username);
        byte[] salt=foundUser.salt;

        String passwordHash = SHAFunction.getShaPwd(password, salt);
        String storedPasswordHash = foundUser.password;
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
            return null;
        }
    }

    public synchronized String getTokoen(String user){
        return tokenbyUsername.get(user);
    }

    public synchronized void addWonGames(String token){
        if(token==null)
            System.out.println("no token : null");
        else {
            UserInDB us = usersbyToken.get(token);
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

        UserInDB us=usersbyToken.get(token);
        return us.wonGames;
    }


    synchronized void addWonGamesFromUsername(String user){

            UserInDB us = userDataTable.get(user);
            if (us == null) {
                System.out.println("no user baby");
            } else {
                us.wonGames++;
                updateFileDB();
            }

    }

    synchronized int getWonGamesFromUsername(String user){
        UserInDB us=userDataTable.get(user);
        return us.wonGames;
    }

    synchronized void addLostGamesFromUsername(String user){

        UserInDB us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.lostGames++;
            updateFileDB();
        }

    }
    synchronized int getLostGamesFromUsername(String user){
        UserInDB us=userDataTable.get(user);
        return us.lostGames;
    }

    synchronized void addAbandonedGamesFromUsername(String user){

        UserInDB us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.abandonedGames++;
            updateFileDB();
        }

    }

    synchronized int getAbandonedGamesFromUsername(String user){
        UserInDB us=userDataTable.get(user);
        return us.abandonedGames;
    }

    synchronized void modifyRankingFromUsername(int pointsToAdd, String user){

        UserInDB us = userDataTable.get(user);
        if (us == null) {
            System.out.println("no user baby");
        } else {
            us.ranking+=pointsToAdd;
            updateFileDB();
        }

    }
    synchronized int getRankingFromUsername(String user){
        UserInDB us=userDataTable.get(user);
        return us.ranking;
    }


    static byte[] getSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private void updateFileDB(){
        UserLoadingFromFile.toFile(userDataTable, "src/main/resources/database/store.db");
    }


}
