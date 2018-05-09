package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.constants.UserDBConstants;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
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


    public synchronized static DatabaseUsers getInstance(){
        if (instance==null) {

            instance = new DatabaseUsers();
            pathFile=UserDBConstants.getPathDbFile();
            try {
                instance.usersByUsername = (HashMap<String, User>) LoadingFromFile.fromFile(pathFile);

            }catch (FileNotFoundException i){
                File f = new File(pathFile);
                instance.usersByUsername =new HashMap<String, User>();
            }
            instance.tokenByUsername =new HashMap<String, String>();

            instance.usersByToken =new HashMap<String, User>();

            instance.socketByToken = new HashMap<>();

            instance.playerInGameByToken=new HashMap<>();
        }
        return instance;
    }

    public synchronized static DatabaseUsers getInstance(String path){
        if (instance==null) {
            instance = new DatabaseUsers();
            pathFile=path;
            try {
                instance.usersByUsername = (HashMap<String, User>) LoadingFromFile.fromFile(pathFile);

            }catch (FileNotFoundException i){
                File f = new File(pathFile);
                instance.usersByUsername =new HashMap<String, User>();
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

        if (usersByUsername.containsKey(username)){
            throw new CannotRegisterUserException(username,0);

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

        }
        catch (PasswordParsingException e){
            throw new CannotRegisterUserException(username,1);
        }
        return newtoken;


    }

    public synchronized String registerUser(String username, String password, Socket newsocket) throws CannotRegisterUserException{
        byte[] salt;
        String passwordHash;
        String newtoken=null;

        if (usersByUsername.containsKey(username)){
            throw new CannotRegisterUserException(username,0);

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
            throw new CannotRegisterUserException(username,1);
        }
        return newtoken;


    }

    public synchronized String login(String username, String password) throws  CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        String newtoken=null;
        PlayerInGame playerInGame=null;
        Socket oldsocket=null;
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
            if (tokenByUsername.containsKey(username))
            {
                String oldtoken= tokenByUsername.get(username);
                playerInGame=playerInGameByToken.get(oldtoken);
                oldsocket= socketByToken.get(oldtoken);
                if (oldsocket!=null) {
                    try {
                        oldsocket.close();
                        socketByToken.remove(oldtoken);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                usersByToken.remove(oldtoken);
            }

            usersByToken.put(newtoken,foundUser);
            tokenByUsername.put(username,newtoken);
            playerInGameByToken.put(newtoken,playerInGame);

            return newtoken;
        }
        else{
            throw new CannotLoginUserException(username,1);
        }
    }

    public synchronized String login(String username, String password, Socket socket) throws  CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        String newtoken=null;
        PlayerInGame playerInGame=null;
        Socket oldsocket=null;
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
            if (tokenByUsername.containsKey(username))
            {
                String oldtoken= tokenByUsername.get(username);
                playerInGame=playerInGameByToken.get(oldtoken);
                oldsocket= socketByToken.get(oldtoken);
                if (oldsocket!=null) {
                    try {
                        oldsocket.close();
                        socketByToken.remove(oldtoken);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                usersByToken.remove(oldtoken);
            }

            usersByToken.put(newtoken,foundUser);
            tokenByUsername.put(username,newtoken);
            playerInGameByToken.put(newtoken,playerInGame);
            socketByToken.put(newtoken, socket);
            return newtoken;
        }
        else{
            throw new CannotLoginUserException(username,1);
        }
    }

    public synchronized String getTokenFromUsername(String user){
        return tokenByUsername.get(user);
    }

    public synchronized String getUsernameByToken(String token) throws NullTokenException, CannotFindUserInDBException {
        if (token == null) throw new NullTokenException();

        String username = usersByToken.get(token).getUsername();
        if (username == null) throw new CannotFindUserInDBException("");
        return username;
    }

    /*public synchronized void setSocketForUser(String token, Socket socket) {
        socketByToken.put(token, socket);
    }
*/


    public synchronized int getWonGamesFromToken (String token) throws NullTokenException, CannotFindUserInDBException {
        if(token==null)
            throw new NullTokenException();

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");

        return us.getWonGames();
    }

    public synchronized int getLostGamesFromToken(String token)throws NullTokenException, CannotFindUserInDBException {
        if(token==null)
            throw new NullTokenException();
        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getLostGames();
    }

    public synchronized int getAbandonedGamesFromToken(String token)throws NullTokenException, CannotFindUserInDBException {
        if(token==null)
            throw new NullTokenException();

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getAbandonedGames();
    }

    public synchronized int getRankingFromToken(String token)throws NullTokenException, CannotFindUserInDBException {
        if(token==null)
            throw new NullTokenException();

        User us= usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getRanking();
    }

    public synchronized void addWonGamesFromToken(String token)throws NullTokenException, CannotFindUserInDBException {
        if(token==null)
            throw new NullTokenException();

        User us = usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
            us.addWonGames();
            updateFileDB();


    }


    public synchronized void addPlayerInGameToDB(PlayerInGame play){
        playerInGameByToken.put(tokenByUsername.get(play.getUser()),play);

    }

    public synchronized void removePlayerInGameFromDB(PlayerInGame play){
        playerInGameByToken.remove(tokenByUsername.get(play.getUser()));
    }


    public synchronized PlayerInGame getPlayerInGameFromToken(String token) throws NullTokenException{
        if(token==null)
            throw new NullTokenException();
        return playerInGameByToken.get(token);
    }

    synchronized void addWonGamesFromUsername(String user)throws CannotFindUserInDBException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);

        us.addWonGames();
        updateFileDB();


    }

    synchronized int getWonGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        return us.getWonGames();
    }

    synchronized void addLostGamesFromUsername(String user) throws CannotFindUserInDBException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);

        us.addLostGames();
        updateFileDB();


    }
    synchronized int getLostGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getWonGames();
    }

    synchronized void addAbandonedGamesFromUsername(String user) throws CannotFindUserInDBException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);

        us.addAbandonedGames();
        updateFileDB();


    }

    synchronized int getAbandonedGamesFromUsername(String user)throws CannotFindUserInDBException {
        User us= usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getAbandonedGames();
    }

    synchronized void addPointsRankingFromUsername(String user, int pointsToAdd) throws CannotFindUserInDBException{

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        us.addPointsToRanking(pointsToAdd);
        updateFileDB();


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


    synchronized void removeSocketFromUsername (String user)throws NullTokenException, NoSocketForUserException{
        Socket oldsocket;
        String token=getTokenFromUsername(user);
        oldsocket=socketByToken.get(token);
        if (oldsocket==null)
                throw new NoSocketForUserException(user);
        try {
            oldsocket.close();
            socketByToken.remove(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateFileDB(){
        LoadingFromFile.toFile(usersByUsername, pathFile);

    }



}
