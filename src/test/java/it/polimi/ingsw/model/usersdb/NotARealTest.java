package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.view.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class NotARealTest
{

    public static synchronized void main(String args[]) throws NoSuchAlgorithmException, IOException {
        DatabaseUsers db= Server.getDb();
        if (db==null)
            System.out.println("no server database babt");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter user:");
        String username=br.readLine();
        System.out.print("Enter password:");
        String password=br.readLine();
        db.registerUser(username,password);
        System.out.print("Enter user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        db.registerUser(username,password);
        System.out.print("Enter user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        db.registerUser(username,password);

        System.out.print("now try login user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        String token=db.login(username,password);
        if(token==null)
            System.out.print("sbagliato\n");
        else {
            System.out.print("giusto giumentoso\n");
            System.out.print("token: "+token+"\n");
        }
        System.out.println("ora provo ad aggiungere una partita\n");
        db.addWonGames(token);
        int voti=db.getWonGames(token);
        System.out.println("\nPartite vinte: "+voti);
        System.out.print("\nnow try login user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        token=db.login(username,password);
        if(token==null)
            System.out.print("sbagliato\n");
        else {
            System.out.print("giusto giumentoso\n");
            System.out.print("token: "+token+"\n");
        }
        System.out.println("ora provo ad aggiungere una partita\n");
        db.addWonGames(token);
        voti=db.getWonGames(token);
        System.out.println("\nPartite vinte: "+voti);





    }


}
