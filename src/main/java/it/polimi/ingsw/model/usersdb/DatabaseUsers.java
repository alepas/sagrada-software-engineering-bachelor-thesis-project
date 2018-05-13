package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.constants.UserDBConstants;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseUsers {
    private static String pathFile;

    private static DatabaseUsers instance ;

    private HashMap<String, User> usersByUsername;


    private HashMap<String, String> tokenByUsername;
    private HashMap<String, User> usersByToken;
    private HashMap<String, Socket> socketByToken;
    private HashMap<String, PlayerInGame> playerInGameByToken;


    public static synchronized DatabaseUsers getInstance(){
        if (instance==null) {

            instance = new DatabaseUsers();
            pathFile=UserDBConstants.getPathDbFile();
            try {
                instance.usersByUsername = (HashMap<String, User>) LoadingFromFile.fromFile(pathFile);

            }catch (DatabaseFileErrorException e) {
                if (e.getErrorId() == 1) {
                    new File(pathFile);
                    instance.usersByUsername = new HashMap<String, User>();
                }
            }
            instance.tokenByUsername =new HashMap<String, String>();

            instance.usersByToken =new HashMap<String, User>();

            instance.socketByToken = new HashMap<>();

            instance.playerInGameByToken=new HashMap<>();
        }
        return instance;
    }

    public static synchronized  DatabaseUsers getInstance(String path){
        if (instance==null) {
            instance = new DatabaseUsers();
            pathFile=path;
            try {
                instance.usersByUsername = (HashMap<String, User>) LoadingFromFile.fromFile(pathFile);

            } catch (DatabaseFileErrorException e) {
                if (e.getErrorId()==1){
                    new File(pathFile);
                    instance.usersByUsername =new HashMap<String, User>();
                }

            }
            instance.tokenByUsername =new HashMap<String, String>();

            instance.usersByToken =new HashMap<String, User>();
            instance.socketByToken = new HashMap<>();
            instance.playerInGameByToken=new HashMap<>();

        }
        return instance;
    }

    private DatabaseUsers(){}


    public synchronized String registerUser(String username, String password) throws CannotRegisterUserException{
        byte[] salt;
        String passwordHash;
        String newtoken=null;
        System.out.println("questo è l'username: "+username);
        if (usersByUsername.containsKey(username)){
            throw new CannotRegisterUserException(username,1);

        }
        System.out.println(">>> Creo nuovo utente "+ username);
        try {
            salt = getSalt();
            passwordHash = SHAFunction.getShaPwd(password, salt);
            User newuser= new User(username, passwordHash,salt);
            usersByUsername.put(username,newuser);
            updateFileDB();
            newtoken = UUID.randomUUID().toString();
            usersByToken.put(newtoken,newuser);
            tokenByUsername.put(username,newtoken);

        }
        catch (PasswordParsingException e){
            throw new CannotRegisterUserException(username,0);
        }
        catch (DatabaseFileErrorException e){
            usersByUsername.remove(username);
            throw new CannotRegisterUserException(username,0);
        }
        return newtoken;


    }

    public synchronized String registerUser(String username, String password, Socket newsocket) throws CannotRegisterUserException {
        byte[] salt;
        String passwordHash;
        String newtoken=null;

        if (usersByUsername.containsKey(username)){
            throw new CannotRegisterUserException(username,1);

        }
        System.out.println(">>> Creo nuovo utente");
        try {
            salt = getSalt();
            passwordHash = SHAFunction.getShaPwd(password, salt);
            User newuser= new User(username, passwordHash,salt);
            usersByUsername.put(username,newuser);
            updateFileDB();
            newtoken = UUID.randomUUID().toString();
            usersByToken.put(newtoken,newuser);
            tokenByUsername.put(username,newtoken);
            socketByToken.put(newtoken,newsocket);

        }
        catch (PasswordParsingException e){
            throw new CannotRegisterUserException(username,0);
        }
        catch (DatabaseFileErrorException e){
            usersByUsername.remove(username);
            throw new CannotRegisterUserException(username,0);
        }

        return newtoken;


    }

    public synchronized String login(String username, String password) throws  CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        String newtoken;
        PlayerInGame playerInGame=null;
        String oldtoken=null;
        User foundUser;

        // username isn't registered
        foundUser= usersByUsername.get(username);
        if(foundUser==null)
            throw new CannotLoginUserException(username,2);


        try {
            byte[] salt=foundUser.getSalt();


            passwordHash = SHAFunction.getShaPwd(password, salt);
        } catch (PasswordParsingException e) {
            throw new CannotLoginUserException(username,0);
        }
        storedPasswordHash = foundUser.getPassword();
        if (passwordHash.equals(storedPasswordHash)){
            newtoken = UUID.randomUUID().toString();
            if ((oldtoken=tokenByUsername.get(username))!=null)
            {

                playerInGame=playerInGameByToken.get(oldtoken);


                try {
                    removeSocketFromToken(oldtoken);
                } catch (CannotCloseOldConnection e) {
                    if (e.getErrorId()==0)
                        throw new CannotLoginUserException(username,3);
                }
                usersByToken.remove(oldtoken);
                playerInGameByToken.remove(oldtoken);

            }


            usersByToken.put(newtoken,foundUser);
            tokenByUsername.put(username,newtoken);
            if (playerInGame!=null)
                playerInGameByToken.put(newtoken,playerInGame);

            return newtoken;
        }
        else{
            throw new CannotLoginUserException(username,1);
        }
    }

    public synchronized String login(String username, String password, Socket socket) throws CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        String newtoken;
        PlayerInGame playerInGame=null;
        String oldtoken=null;

        // username isn't registered
        if(!usersByUsername.containsKey(username) ){
            throw new CannotLoginUserException(username,2);
        }
        User foundUser=null;
        foundUser= usersByUsername.get(username);
        try {
            byte[] salt=foundUser.getSalt();


            passwordHash = SHAFunction.getShaPwd(password, salt);
        } catch (PasswordParsingException e) {
            throw new CannotLoginUserException(username,0);
        }
        storedPasswordHash = foundUser.getPassword();
        if (passwordHash.equals(storedPasswordHash)){
            newtoken = UUID.randomUUID().toString();
            if ((oldtoken=tokenByUsername.get(username))!=null)
            {

                playerInGame=playerInGameByToken.get(oldtoken);


                    try {
                        removeSocketFromToken(oldtoken);
                    } catch (CannotCloseOldConnection e) {
                        if (e.getErrorId()==0)
                            throw new CannotLoginUserException(username,3);
            }
                usersByToken.remove(oldtoken);
                playerInGameByToken.remove(oldtoken);

            }

            usersByToken.put(newtoken,foundUser);
            tokenByUsername.put(username,newtoken);
            if (playerInGame!=null)
                playerInGameByToken.put(newtoken,playerInGame);
            socketByToken.put(newtoken, socket);
            return newtoken;
        }
        else{
            throw new CannotLoginUserException(username,1);
        }
    }

    synchronized String getTokenFromUsername(String user) throws CannotFindUserInDBException {
        String token;
        token=tokenByUsername.get(user);
        if (token==null)
            throw new CannotFindUserInDBException(user);
        return token;
    }

    public synchronized String getUsernameByToken(String token) throws CannotFindUserInDBException {
        if (token == null) throw new CannotFindUserInDBException("");

        String username = usersByToken.get(token).getUsername();
        if (username == null) throw new CannotFindUserInDBException("");
        return username;
    }

    public synchronized int getWonGamesFromToken (String token) throws  CannotFindUserInDBException {
        if(token==null)
            throw new CannotFindUserInDBException("");

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");

        return us.getWonGames();
    }

    public synchronized int getLostGamesFromToken(String token)throws  CannotFindUserInDBException {
        if(token==null)
            throw new CannotFindUserInDBException("");
        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getLostGames();
    }

    public synchronized int getAbandonedGamesFromToken(String token)throws  CannotFindUserInDBException {
        if(token==null)
            throw new CannotFindUserInDBException("");

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getAbandonedGames();
    }

    public synchronized int getRankingFromToken(String token)throws  CannotFindUserInDBException {
        if(token==null)
            throw new CannotFindUserInDBException("");

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getRanking();
    }


    public synchronized void addPlayerInGameToDB(PlayerInGame play) throws CannotAddPlayerInGameException {
        String token=tokenByUsername.get(play.getUser());
        if (token==null)
            throw new CannotAddPlayerInGameException();
        playerInGameByToken.put(token,play);

    }

    public synchronized void removePlayerInGameFromDB(PlayerInGame play) throws CannotFindPlayerInGameException {
        String token=tokenByUsername.get(play.getUser());
        if (token==null)
            throw new CannotFindPlayerInGameException();
        playerInGameByToken.remove(token);
    }


    public synchronized PlayerInGame getPlayerInGameFromToken(String token) throws CannotFindPlayerInGameException {
        PlayerInGame play;
        if(token==null)
            throw new CannotFindPlayerInGameException();
        play=playerInGameByToken.get(token);
        if (play==null)
            throw new CannotFindPlayerInGameException();
        return play;
    }

    synchronized void addWonGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user,0);
        us.addWonGames();
        try {
            updateFileDB();
        }
        catch (DatabaseFileErrorException e){
            us.removeWonGames();
            throw new CannotUpdateStatsForUserException(user,1);
        }


    }

    synchronized int getWonGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getWonGames();
    }

    synchronized void addLostGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user,0);

        us.addLostGames();
        try {
            updateFileDB();
        }
        catch (DatabaseFileErrorException e){
            us.removeLostGames();
            throw new CannotUpdateStatsForUserException(user,1);
        }


    }
    synchronized int getLostGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getLostGames();
    }

    synchronized void addAbandonedGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user,0);

        us.addAbandonedGames();
        try {
            updateFileDB();
        }
        catch (DatabaseFileErrorException e){
            us.removeAbandonedGames();
            throw new CannotUpdateStatsForUserException(user,1);
        }

    }

    synchronized int getAbandonedGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getAbandonedGames();
    }

    synchronized void addPointsRankingFromUsername(String user, int pointsToAdd) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user,0);
        us.addPointsToRanking(pointsToAdd);
        try {
            updateFileDB();
        }
        catch (DatabaseFileErrorException e){
            us.addPointsToRanking(0-pointsToAdd);
            throw new CannotUpdateStatsForUserException(user,1);
        }


    }
    synchronized int getRankingFromUsername(String user) throws CannotFindUserInDBException{
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getRanking();
    }


    static synchronized byte[] getSalt() throws PasswordParsingException {
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


    synchronized void removeSocketFromUsername (String user) throws CannotCloseOldConnection {
        Socket oldsocket;
        String token=tokenByUsername.get(user);
        if (token==null){
            throw new CannotCloseOldConnection(user,1);
        }
        oldsocket=socketByToken.get(token);
        if (oldsocket==null)
                throw new CannotCloseOldConnection(user,1);
        try {
            oldsocket.close();
            socketByToken.remove(token);
        } catch (IOException e) {
            throw new CannotCloseOldConnection(user,0);
        }
    }

    synchronized void removeSocketFromToken (String token) throws CannotCloseOldConnection {
        Socket oldsocket;
        if (token==null){
            throw new CannotCloseOldConnection("",1);
        }
        oldsocket=socketByToken.get(token);
        if (oldsocket==null)
            throw new CannotCloseOldConnection("",1);
        try {
            oldsocket.close();
            socketByToken.remove(token);
        } catch (IOException e) {
            throw new CannotCloseOldConnection("",0);
        }
    }

    private synchronized void updateFileDB() throws DatabaseFileErrorException {
        try {
            LoadingFromFile.toFile(usersByUsername, pathFile);
        } catch (DatabaseFileErrorException e) {
            updateFileDB(1);
        }

    }

    private synchronized void updateFileDB(int attemp) throws DatabaseFileErrorException {
        if (attemp<3){
        try {
            LoadingFromFile.toFile(usersByUsername, pathFile);
        } catch (DatabaseFileErrorException e) {
            updateFileDB(++attemp);
        }}
        throw new DatabaseFileErrorException(0);

    }


}
