package server.model.users;


import server.constants.UserDBConstants;
import server.model.game.Game;
import server.model.gamesdb.DatabaseGames;
import shared.clientInfo.ClientUser;
import shared.exceptions.gameExceptions.CannotCreatePlayerException;
import shared.exceptions.gameExceptions.InvalidGameParametersException;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

/**
 * is the database of users and players. It keeps also the token references to the current sessions.
 */
public class DatabaseUsers {
    private static String pathFile;

    private static DatabaseUsers instance;
    private DatabaseGames databaseGames;

    private HashMap<String, User> usersByUsername;


    private HashMap<String, String> tokenByUsername;
    private HashMap<String, User> usersByToken;
    private HashMap<String, PlayerInGame> playerInGameByToken;
    private HashMap<String, PlayerInGame> playerByUsername;

    /**
     * @return the instance of the Users Database of the server. If there is no instance, it creates a new one.
     */
    public static synchronized DatabaseUsers getInstance() {
        if (instance == null) {
            instance = new DatabaseUsers(UserDBConstants.getPathDbFile());
        }
        return instance;
    }


    /**
     * Method used to get the instance of the Users Database of the server with a given path for the database file.
     * This method is used to initialize the database during tests, without modifying the real users database.
     *
     * @param path is the path of the database file.
     * @return the instance of the Users Database of the server.
     */
    public static synchronized DatabaseUsers getInstance(String path) {
        if (instance == null) {
            instance = new DatabaseUsers(path);

        }
        return instance;
    }


    /**
     * Private constructor of the users database. It calls the function to load the database from file and sets up the
     * maps for saving the correspondences between users, tokens, players. Then it saves a link to the database of current games.
     *
     * @param path is the path of the database file.
     */
    private DatabaseUsers(String path) {
        pathFile = path;

        if (path==null)
            pathFile=UserDBConstants.getPathDbFile();

        tokenByUsername = new HashMap<>();

        usersByToken = new HashMap<>();

        playerInGameByToken = new HashMap<>();

        playerByUsername = new HashMap<>();

        databaseGames = DatabaseGames.getInstance();

        loadDatabaseFromFile();
    }


    //-------------------- DATABASE SET-UP methods ----------------------------


    /**
     * Loads the database from the file and stores the hashMap received in the users database.
     * If the file does not exists or some problems occur during the reading process, it creates a new hashmap to store the
     * correspondences between User Objects and username.
     */
    private void loadDatabaseFromFile() {
        try {
            usersByUsername = (HashMap<String, User>) FileObjectSerialization.fromFile(pathFile);

        } catch (DatabaseFileErrorException e) {
            if (e.getErrorId() == 1) {
                new File(pathFile);
                usersByUsername = new HashMap<>();
            }
        }
    }


    /**
     * Calls the method to update the database file stored on server. If some error occur try again for 3 times and then,
     * if it didn't work, throws an exception
     *
     * @param attemp is the number of the attemp to update the db file
     * @throws DatabaseFileErrorException if any error occur for 3 times
     */
    private synchronized void updateFileDB(int attemp) throws DatabaseFileErrorException {
        if (attemp < 3) {
            try {
                FileObjectSerialization.toFile(usersByUsername, pathFile);
            } catch (DatabaseFileErrorException e) {
                updateFileDB(++attemp);
            }
        }
        else throw new DatabaseFileErrorException(0);

    }


    //-------------------------- Login, Registration, Session ---------------------------------


    /**
     * Register a new user in in the database.
     *
     * @param username is the username of the user that wants to be registered.
     * @param password is the password of the user that wants to be registered.
     * @return the TOKEN of the session that has to be used by the client to make the next calls to the server
     * @throws CannotRegisterUserException if the username has already been taken or if some problems occur in the
     *                                     registration process.
     */
    public synchronized String registerUser(String username, String password) throws CannotRegisterUserException {
        byte[] salt;
        String passwordHash;
        String newtoken;
        if (usersByUsername.containsKey(username)) {
            //the username has been already taken
            throw new CannotRegisterUserException(username, 1);
        }
        try {
            salt = getSalt();
            passwordHash = SHAFunction.getShaPwd(password, salt);
            User newuser = new User(username, passwordHash, salt);
            usersByUsername.put(username, newuser);
            updateFileDB(0);
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


    /**
     * Logs the user in if the username and the password are right.
     *
     * @param username is the username of the user that wants to log in.
     * @param password is the password of the user that wants to log in.
     * @return the TOKEN of the session that has to be used by the client to make the next calls to the server.
     * @throws CannotLoginUserException if the username does not correspond to any registered user, if the password is wrong
     *                                  or if some problems occur during the log in process.
     */
    public synchronized String login(String username, String password) throws CannotLoginUserException {
        String passwordHash;
        String storedPasswordHash;
        User foundUser;
        foundUser = usersByUsername.get(username);
        //username is not present in database
        if (foundUser == null)
            throw new CannotLoginUserException(username, 2);
        //username is present in database
        try {
            byte[] salt = foundUser.getSalt();
            passwordHash = SHAFunction.getShaPwd(password, salt);
        } catch (PasswordParsingException e) {
            throw new CannotLoginUserException(username, 0);
        }
        storedPasswordHash = foundUser.getPassword();
        if (passwordHash.equals(storedPasswordHash)) {
            //the password matches the one in database
            String newtoken = UUID.randomUUID().toString();
            usersByToken.put(newtoken, foundUser);
            tokenByUsername.put(username, newtoken);
            return newtoken;
        } else {
            throw new CannotLoginUserException(username, 1);
        }
    }


    /**
     * Calculate the random SALT used to hash the password for the new user.
     *
     * @return the random SALT generated.
     * @throws PasswordParsingException if the SecureRandom called function is not able to create a new salt.
     */
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


    /**
     * Deletes all the references in tables for the current session of a user. If there is a "player in game" connected
     * to the session, the method calls the disconnect one of the "player in game"
     *
     * @param userToken is the token of the session that will be removed
     */
    public void removeSession(String userToken) {
        String username;
        try {
            username = getUsernameByToken(userToken);
            if (tokenByUsername.get(username).equals(userToken))
                tokenByUsername.remove(username);
        } catch (CannotFindUserInDBException e) {
            //do nothing
        }
        PlayerInGame player = playerInGameByToken.get(userToken);
        if (player != null)
            player.disconnect();
        usersByToken.remove(userToken);
        playerInGameByToken.remove(userToken);
    }


    //------------------------ Getters of Usernames and Players ------------------------------


    /**
     * Returns the username of the user corresponding to a given session using the token
     *
     * @param token is the token of the session
     * @return the username of the user if it is found in the database
     * @throws CannotFindUserInDBException if it is impossible to get the user form the given token or the token is null
     */
    private synchronized String getUsernameByToken(String token) throws CannotFindUserInDBException {
        if (token == null) throw new CannotFindUserInDBException("");

        User user = usersByToken.get(token);
        if (user == null)
            throw new CannotFindUserInDBException("");
        String username = user.getUsername();
        if (username == null) throw new CannotFindUserInDBException("");
        return username;
    }


    /**
     * Returns the PlayerInGame Object corresponding to a user with an active session identified by the token
     *
     * @param token is the token of the session
     * @return the corresponding PlayerInGame
     * @throws CannotFindPlayerInDatabaseException if there is no Player linked to the session
     */
    public synchronized PlayerInGame getPlayerInGameFromToken(String token) throws CannotFindPlayerInDatabaseException {
        return getPlayer(token, false);
    }

    /**
     * Returns the PlayerInGame Object corresponding to a user identified by the token. This method works even if the user
     * has no active sessions.
     *
     * @param username is the username of the user that wants to find the PlayerInGame Object
     * @return the corresponding PlayerInGame
     * @throws CannotFindPlayerInDatabaseException if there is no Player linked to the username
     */
    public synchronized PlayerInGame getPlayerInGameFromUsername(String username) throws CannotFindPlayerInDatabaseException {
        return getPlayer(username, true);
    }


    /**
     * Locates in the database the PlayerInGame Object corresponding to a user with or without an active session.
     * If the key passed is a username, the player can be found even if there are no active sessions for the corresponding user.
     * If the key passed is a token, the player can be found only if there is an active session for the corresponding user.
     *
     * @param key      is the String used for the search [username or token]
     * @param username has to be set "true" if the key is the username, "false" if it is the token
     * @return the corresponding Player In Game
     * @throws CannotFindPlayerInDatabaseException if the function can not find the Player in the database
     */
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


    //-------------------------- Players and Games ------------------------------


    /**
     * Stores the PlayerInGame Object in the database in two different maps on the server.
     * In a map it will be saved with the username as key, in the other one using the current session token as key only
     * if there is a session present
     *
     * @param player is the PlayerInGame Objects that has to be saved
     */
    synchronized void addPlayerInGameToDB(PlayerInGame player) {
        String token = tokenByUsername.get(player.getUser());
        if (token != null)
            playerInGameByToken.put(token, player);
        playerByUsername.put(player.getUser(), player);
    }


    /**
     * Removes the PlayerInGame Object from the database, if present.
     *
     * @param player the Player Object to remove
     */
    public synchronized void removePlayerInGameFromDB(PlayerInGame player) {
        String token = tokenByUsername.get(player.getUser());
        if (token != null)
            playerInGameByToken.remove(token);
        playerByUsername.remove(player.getUser());
    }


    /**
     * Verify if the user of the given session has a PlayerInGame stored in the database. If there is a player,
     * the method stores the new correspondence between the new token and the player. Then it calls the the method of
     * the player to set the player as "connected".
     *
     * @param token is the token of the current session
     * @return the PlayerInGame if found
     * @throws CannotFindPlayerInDatabaseException if there was a problem finding the game in the database or if there
     * is no player stored in the database for the user related to the given session
     */
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
        play.reConnect();
        playerInGameByToken.put(token, play);
        return play;
    }


    /**
     * Returns a new Game for the user of the given session.
     * If there is no old player object present in the database for the user,
     * the method calls findGameForUser of the Game Database to find an already created game or to create a new one
     *
     * @param userToken is the token of the current session
     * @param numPlayers is the number of Players chosen for the new game
     * @param levelOfDifficulty is the level of difficulty chosen for the new game
     * @return the new Game is created or found
     * @throws InvalidGameParametersException if findGameForUser does not accept the number of players
     * @throws CannotCreatePlayerException if findGameForUser can not create the player Object for the user in the found game
     * @throws CannotFindUserInDBException if the current session can not be found in the database
     */
    public synchronized Game findNewGame(String userToken, int numPlayers, int levelOfDifficulty) throws InvalidGameParametersException, CannotCreatePlayerException, CannotFindUserInDBException {
        String username = getUsernameByToken(userToken);
        PlayerInGame player = playerByUsername.get(username);
        if (player != null) {
            throw new CannotCreatePlayerException(player.getUser());
        }
        return databaseGames.findGameForUser(username, numPlayers, levelOfDifficulty);
    }


    //-------------------------- Users and statistics ------------------------------


    /**
     * Return the clientUser of the user corresponding the the session identified by token
     * The clientUser contains all the statistics of the user: won games, lost games, abandoned games, ranking
     *
     * @param token is the token of the current session
     * @return the ClientUser corresponding object
     * @throws CannotFindUserInDBException if the user can not be found in the database
     */
    public synchronized ClientUser getClientUserByToken(String token) throws CannotFindUserInDBException {
        if (token == null) throw new CannotFindUserInDBException("");

        User user = usersByToken.get(token);
        if (user == null) throw new CannotFindUserInDBException("");

        return user.getClientUser();
    }


    /**
     * Adds a won game to a user in the database
     *
     * @param username is the username of the player that won a game
     * @throws CannotUpdateStatsForUserException if the user can not be found in the database or if sone error occour
     * during the update of the database
     */
    synchronized void addWonGamesFromUsername(String username) throws CannotUpdateStatsForUserException {
        User us = usersByUsername.get(username);
        if (us == null)
            throw new CannotUpdateStatsForUserException(username, 0);
        us.addWonGames();
        try {
            updateFileDB(0);
        } catch (DatabaseFileErrorException e) {
            us.removeWonGames();
            throw new CannotUpdateStatsForUserException(username, 1);
        }
    }

    /**
     * Adds a lost game to a user in the database
     *
     * @param username is the username of the player that lost a game
     * @throws CannotUpdateStatsForUserException if the user can not be found in the database or if sone error occour
     * during the update of the database
     */
    synchronized void addLostGamesFromUsername(String username) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(username);
        if (us == null)
            throw new CannotUpdateStatsForUserException(username, 0);

        us.addLostGames();
        try {
            updateFileDB(0);
        } catch (DatabaseFileErrorException e) {
            us.removeLostGames();
            throw new CannotUpdateStatsForUserException(username, 1);
        }


    }

    /**
     * Adds an abandoned game to a user in the database
     *
     * @param username is the username of the player that abandoned a game
     * @throws CannotUpdateStatsForUserException if the user can not be found in the database or if sone error occour
     * during the update of the database
     */
    synchronized void addAbandonedGamesFromUsername(String username) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(username);
        if (us == null)
            throw new CannotUpdateStatsForUserException(username, 0);

        us.addAbandonedGames();
        try {
            updateFileDB(0);
        } catch (DatabaseFileErrorException e) {
            us.removeAbandonedGames();
            throw new CannotUpdateStatsForUserException(username, 1);
        }

    }


    /**
     * Adds the player points to the ranking of the corresponding user
     *
     * @param username is the username of the player
     * @param pointsToAdd is the number of points that the player got in the game
     * @throws CannotUpdateStatsForUserException f the user can not be found in the database or if sone error occour
     * during the update of the database
     */
    synchronized void addPointsRankingFromUsername(String username, int pointsToAdd) throws CannotUpdateStatsForUserException {

        User us = usersByUsername.get(username);
        if (us == null)
            throw new CannotUpdateStatsForUserException(username, 0);
        us.addPointsToRanking(pointsToAdd);
        try {
            updateFileDB(0);
        } catch (DatabaseFileErrorException e) {
            us.addPointsToRanking(0 - pointsToAdd);
            throw new CannotUpdateStatsForUserException(username, 1);
        }


    }


}
