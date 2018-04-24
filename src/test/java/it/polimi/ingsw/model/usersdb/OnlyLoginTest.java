package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.exceptions.userExceptions.CannotLoginUserException;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class OnlyLoginTest
{

    public synchronized static void main(String args[]) throws NoSuchAlgorithmException, IOException {
        DatabaseUsers db = DatabaseUsers.getInstance();
        String token = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("db address: " + db.toString());

        for (int i = 0; i < 5; i++) {
            token=null;
            System.out.print("now try login user:");
            String username = br.readLine();
            System.out.print("Enter password:");
            String password = br.readLine();
            try {
                token = db.login(username, password);
                System.out.print("giusto\n");
                System.out.print("token: " + token + "\n");
                System.out.println("ora provo ad aggiungere una partita\n");
                if (db == null)
                    System.out.println("niente db");
                db.addWonGames(token);
                int voti = db.getWonGames(token);
                System.out.println("\nPartite vinte: " + voti);
            } catch (CannotLoginUserException e) {
                System.out.print(e.getMessage());
            }
        }
    }

}
