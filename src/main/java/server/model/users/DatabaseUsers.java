package server.model.users;


import server.constants.UserDBConstants;
import server.model.game.Game;
import server.model.gamesdb.DatabaseGames;
import shared.clientInfo.ClientUser;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidNumOfPlayersException;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Observer;
import java.util.UUID;

public class DatabaseUsers {
    private static String pathFile;

    private static DatabaseUsers instance;
    private DatabaseGames databaseGames;

    private HashMap<String, User> usersByUsername;


    private HashMap<String, String> tokenByUsername;
    private HashMap<String, User> usersByToken;
    private HashMap<String, PlayerInGame> playerInGameByToken;
    private HashMap<String, PlayerInGame> playerByUsername;


    public static synchronized DatabaseUsers getInstance() {
        if (instance == null) {

            instance = new DatabaseUsers();
            pathFile = UserDBConstants.getPathDbFile();

            instance.loadDatabaseFromFile();

            instance.tokenByUsername = new HashMap<String, String>();

            instance.usersByToken = new HashMap<String, User>();

            instance.playerInGameByToken = new HashMap<>();

            instance.playerByUsername = new HashMap<>();

            instance.databaseGames = DatabaseGames.getInstance();

        }
        return instance;
    }

    public static synchronized DatabaseUsers getInstance(String path) {
        if (instance == null) {
            instance = new DatabaseUsers();
            pathFile = path;
            instance.loadDatabaseFromFile();
            instance.tokenByUsername = new HashMap<String, String>();

            instance.usersByToken = new HashMap<String, User>();
            instance.playerInGameByToken = new HashMap<>();
            instance.playerByUsername = new HashMap<>();

        }
        return instance;
    }


    private DatabaseUsers() { }



    public void removeClient(String userToken){
        String username;
        try {
            username = getUsernameByToken(userToken);
            if (tokenByUsername.get(username).equals(userToken))
                tokenByUsername.remove(username);
        } catch (CannotFindUserInDBException e) {
            //do nothing
        }
        PlayerInGame player = playerInGameByToken.get(userToken);
        if (player!=null)
            player.disconnect();
        usersByToken.remove(userToken);
        playerInGameByToken.remove(userToken);
    }

    public synchronized String registerUser(String username, String password) throws CannotRegisterUserException {
        byte[] salt;
        String passwordHash;
        String newtoken = null;
        if (usersByUsername.containsKey(username)) {
            throw new CannotRegisterUserException(username, 1);

        }
        System.out.println(">>> Creo nuovo utente " + username);
        try {
            salt = getSalt();
            passwordHash = SHAFunction.getShaPwd(password, salt);
            User newuser = new User(username, passwordHash, salt);
            usersByUsername.put(username, newuser);
            updateFileDB();
            newtoken = UUID.randomUUID().toString();
            usersByToken.put(newtoken, newuser);
            tokenByUsername.put(username, newtoken);

        } catch (PasswordParsingException e) {
            throw new CannotRegisterUserException(username, 0);
        } catch (DatabaseFileErrorException e) {
            usersByUsername.remove(username);
            throw new CannotRegisterUserException(username, 0);
        }
        return newtoken;


    }

    public synchronized String login(String username, String password) throws CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        User foundUser;

        // username isn't registered
        foundUser = usersByUsername.get(username);
        if (foundUser == null)
            throw new CannotLoginUserException(username, 2);


        try {
            byte[] salt = foundUser.getSalt();


            passwordHash = SHAFunction.getShaPwd(password, salt);
        } catch (PasswordParsingException e) {
            throw new CannotLoginUserException(username, 0);
        }
        storedPasswordHash = foundUser.getPassword();
        if (passwordHash.equals(storedPasswordHash)) {
            String newtoken = UUID.randomUUID().toString();
            usersByToken.put(newtoken, foundUser);
            tokenByUsername.put(username, newtoken);

            return newtoken;
        } else {
            throw new CannotLoginUserException(username, 1);
        }
    }

    public synchronized ClientUser getClientUserByToken(String token) throws CannotFindUserInDBException {
        if (token == null) throw new CannotFindUserInDBException("");

        User user = usersByToken.get(token);
        if (user == null) throw new CannotFindUserInDBException("");

        return user.getClientUser();
    }


    synchronized String getUsernameByToken(String token) throws CannotFindUserInDBException {
        if (token == null) throw new CannotFindUserInDBException("");

        User user = usersByToken.get(token);
        if (user==null)
            throw new CannotFindUserInDBException("");
        String username=user.getUsername();
        if (username == null) throw new CannotFindUserInDBException("");
        return username;
    }

    //TODO rimuovere questi metodi se non servono
    /*
     synchronized int getWonGamesFromToken(String token) throws CannotFindUserInDBException {
        if (token == null)
            throw new CannotFindUserInDBException("");

        User us = usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");

        return us.getWonGames();
    }


    synchronized int getLostGamesFromToken(String token) throws CannotFindUserInDBException {
        if (token == null)
            throw new CannotFindUserInDBException("");
        User us = usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getLostGames();
    }

    synchronized int getAbandonedGamesFromToken(String token) throws CannotFindUserInDBException {
        if (token == null)
            throw new CannotFindUserInDBException("");

        User us = usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getAbandonedGames();
    }

    synchronized int getRankingFromToken(String token) throws CannotFindUserInDBException {
        if (token == null)
            throw new CannotFindUserInDBException("");

        User us = usersByToken.get(token);
        if (us == null)
            throw new CannotFindUserInDBException("");
        return us.getRanking();
    }
*/


    synchronized void addPlayerInGameToDB(PlayerInGame play) throws CannotAddPlayerInDatabaseException {
        String token = tokenByUsername.get(play.getUser());
        if (token == null)
            throw new CannotAddPlayerInDatabaseException();
        playerInGameByToken.put(token, play);
        playerByUsername.put(play.getUser(), play);
    }

    public synchronized void removePlayerInGameFromDB(PlayerInGame play) throws CannotFindPlayerInDatabaseException {
        String token = tokenByUsername.get(play.getUser());
        if (token != null)
            playerInGameByToken.remove(token);
        playerByUsername.remove(play.getUser());
    }


    public synchronized PlayerInGame getPlayerInGameFromToken(String token) throws CannotFindPlayerInDatabaseException {
        return getPlayer(token,false);
    }

    public synchronized PlayerInGame getPlayerInGameFromUsername(String username) throws CannotFindPlayerInDatabaseException {
        return getPlayer(username,true);
    }

    private PlayerInGame getPlayer(String key, boolean username) throws CannotFindPlayerInDatabaseException {
        PlayerInGame play;
        if (key == null)
            throw new CannotFindPlayerInDatabaseException();
        if (username)
        play = playerByUsername.get(key);
        else play = playerInGameByToken.get(key);
        if (play == null)
            throw new CannotFindPlayerInDatabaseException();
        return play;
    }

    synchronized void addWonGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user, 0);
        us.addWonGames();
        try {
            updateFileDB();
        } catch (DatabaseFileErrorException e) {
            us.removeWonGames();
            throw new CannotUpdateStatsForUserException(user, 1);
        }


    }

    synchronized int getWonGamesFromUsername(String user) throws CannotFindUserInDBException {
        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getWonGames();
    }

    synchronized void addLostGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user, 0);

        us.addLostGames();
        try {
            updateFileDB();
        } catch (DatabaseFileErrorException e) {
            us.removeLostGames();
            throw new CannotUpdateStatsForUserException(user, 1);
        }


    }

    synchronized int getLostGamesFromUsername(String user) throws CannotFindUserInDBException {
        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getLostGames();
    }

    synchronized void addAbandonedGamesFromUsername(String user) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user, 0);

        us.addAbandonedGames();
        try {
            updateFileDB();
        } catch (DatabaseFileErrorException e) {
            us.removeAbandonedGames();
            throw new CannotUpdateStatsForUserException(user, 1);
        }

    }

    synchronized int getAbandonedGamesFromUsername(String user) throws CannotFindUserInDBException {
        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getAbandonedGames();
    }

    synchronized void addPointsRankingFromUsername(String user, int pointsToAdd) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotUpdateStatsForUserException(user, 0);
        us.addPointsToRanking(pointsToAdd);
        try {
            updateFileDB();
        } catch (DatabaseFileErrorException e) {
            us.addPointsToRanking(0 - pointsToAdd);
            throw new CannotUpdateStatsForUserException(user, 1);
        }


    }

    synchronized int getRankingFromUsername(String user) throws CannotFindUserInDBException {
        User us = usersByUsername.get(user);
        if (us == null)
            throw new CannotFindUserInDBException(user);
        return us.getRanking();
    }


    private static synchronized byte[] getSalt() throws PasswordParsingException {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordParsingException();
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }



    private synchronized void updateFileDB() throws DatabaseFileErrorException {
        try {
            LoadingFromFile.toFile(usersByUsername, pathFile);
        } catch (DatabaseFileErrorException e) {
            updateFileDB(1);
        }

    }

    private synchronized void updateFileDB(int attemp) throws DatabaseFileErrorException {
        if (attemp < 3) {
            try {
                LoadingFromFile.toFile(usersByUsername, pathFile);
            } catch (DatabaseFileErrorException e) {
                updateFileDB(++attemp);
            }
        }
        throw new DatabaseFileErrorException(0);

    }

    public synchronized PlayerInGame findOldPlayer(String token) throws CannotFindPlayerInDatabaseException {
        PlayerInGame play;
        String username;
        try {
            username = getUsernameByToken(token);
        } catch (CannotFindUserInDBException e) {
            throw new CannotFindPlayerInDatabaseException();
        }
        play = playerByUsername.get(username);
        if (play == null)
            throw new CannotFindPlayerInDatabaseException();
        play.setDisconnected(false);
        playerInGameByToken.put(token, play);
        return play;
    }

    public synchronized Game findNewGame(String userToken, int numPlayers, int levelOfDifficulty, Observer observer) throws InvalidNumOfPlayersException, CannotCreatePlayerException, CannotFindUserInDBException {
        String username = getUsernameByToken(userToken);
        PlayerInGame player = playerByUsername.get(username);
        if (player != null) {
            throw new CannotCreatePlayerException(player.getUser());
        }
        Game game = databaseGames.findGameForUser(username, numPlayers, levelOfDifficulty);
        game.addObserver(observer);
        return game;
    }



    private void loadDatabaseFromFile() {
        try {
            instance.usersByUsername = (HashMap<String, User>) LoadingFromFile.fromFile(pathFile);

        } catch (DatabaseFileErrorException e) {
            if (e.getErrorId() == 1) {
                new File(pathFile);
                instance.usersByUsername = new HashMap<String, User>();
            }
        }
    }


}
