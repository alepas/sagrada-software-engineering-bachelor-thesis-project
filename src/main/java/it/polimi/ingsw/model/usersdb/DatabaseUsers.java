package it.polimi.ingsw.model.usersdb;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseUsers {



   // private static DatabaseUsers instance=null ;

    private static ConcurrentHashMap<String, UserInDB> userDataTable;

    private static ConcurrentHashMap<String, String> tokenbyUsername;
    private static ConcurrentHashMap<String, UserInDB> usersbyToken;


    private static final DatabaseUsers instance = new DatabaseUsers();

    protected DatabaseUsers(){

        try {
            userDataTable = (ConcurrentHashMap<String, UserInDB>) UserLoadingFromFile.fromFile("src/main/resources/database/store.db");

        }catch (FileNotFoundException i){
            File f = new File("src/main/resources/database/store.db");
            userDataTable=new ConcurrentHashMap<String, UserInDB>();


        }
        tokenbyUsername= new ConcurrentHashMap<String, String>();
        usersbyToken=  new ConcurrentHashMap<String, UserInDB>();
        System.out.println("usersbyToken "+usersbyToken);

    }

    public static DatabaseUsers getInstance() {
        return instance;
    }

   /* public synchronized static DatabaseUsers getInstance(){
        if (instance==null) {
            System.out.println("not found instance "+instance);

            instance = new DatabaseUsers();
            try {
                userDataTable = (ConcurrentHashMap<String, UserInDB>) UserLoadingFromFile.fromFile("src/main/resources/database/store.db");

            }catch (FileNotFoundException i){
                File f = new File("src/main/resources/database/store.db");
                userDataTable=new ConcurrentHashMap<String, UserInDB>();


            }
            tokenbyUsername= new ConcurrentHashMap<String, String>();
            usersbyToken=  new ConcurrentHashMap<String, UserInDB>();
            System.out.println("usersbyToken "+usersbyToken);
        }
        return instance;
    }
*/
    // This class is a singleton. Call getInstance() instead.
    // private DatabaseUsers(){}

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
            String passwordHash = SHAFunction.getShaPwd(password, salt);
            UserInDB newuser= new UserInDB();
            newuser.password=passwordHash;
            newuser.salt=salt;
            newuser.wonGames=0;
            newuser.lostGames=0;
            newuser.abandonedGames=0;
            newuser.ranking=0;

/*            byte[] userserial=null;
            try {
                userserial=BytesUtil.toByteArray(newuser);
            } catch (IOException e) {
                e.printStackTrace();
            }

            */
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
      //  try {

            foundUser= userDataTable.get(username);

 /*       } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        byte[] salt=foundUser.salt;

        String passwordHash = SHAFunction.getShaPwd(password, salt);
        String storedPasswordHash = foundUser.password;
        if (passwordHash.equals(storedPasswordHash)){
            String newtoken = UUID.randomUUID().toString();
            if (tokenbyUsername.containsKey(username))
            {
                String oldtoken=tokenbyUsername.get(username);
              //  UserInDB loggedUser=usersbyToken.get(oldtoken);

                usersbyToken.remove(oldtoken);


            }
            else { }


            usersbyToken.put(newtoken,foundUser);
            tokenbyUsername.put(username,newtoken);

            return newtoken;
        }
        else{
            return null;
        }
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

    static synchronized byte[] getSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private synchronized void updateFileDB(){
        UserLoadingFromFile.toFile(userDataTable, "src/main/resources/database/store.db");
    }



}
