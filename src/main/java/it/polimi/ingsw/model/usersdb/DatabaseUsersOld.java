package it.polimi.ingsw.model.usersdb;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.UUID;

public class DatabaseUsersOld {



    private static DatabaseUsersOld instance ;

    private Hashtable<String, byte[]> userDataTable=null;

    private Hashtable<String, String> tokenbyUsername = new Hashtable<>();
    private Hashtable<String, UserInDB> usersbyToken = new Hashtable<>();


    public synchronized static DatabaseUsersOld getInstance(){
        if (instance==null) {
            instance = new DatabaseUsersOld();
            try {
                instance.userDataTable = (Hashtable<String, byte[]>) UserLoadingFromFile.fromFile("src/main/resources/database/store.db");

            }catch (FileNotFoundException i){
                File f = new File("src/main/resources/database/store.db");
                instance.userDataTable=new Hashtable<String, byte[]>();
            }

        }
        return instance;
    }

    // This class is a singleton. Call getInstance() instead.
    private DatabaseUsersOld(){}

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
            byte[] userserial=null;
            try {
                userserial=BytesUtil.toByteArray(newuser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userDataTable.put(username,userserial);
            UserLoadingFromFile.toFile(userDataTable, "src/main/resources/database/store.db");
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
        try {
            byte[] mio=userDataTable.get(username);
            foundUser= BytesUtil.toObject(mio);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        UserInDB us=usersbyToken.get(token);
        us.wonGames++;
    }
    public synchronized int getWonGames(String token){
        UserInDB us=usersbyToken.get(token);
       return us.wonGames;
    }

    static synchronized byte[] getSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }





}
