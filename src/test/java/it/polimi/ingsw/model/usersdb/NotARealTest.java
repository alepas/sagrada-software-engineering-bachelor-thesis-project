package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class NotARealTest {
        public static void main(String[] args) throws Exception {

                DatabaseUsers db = DatabaseUsers.getInstance();

                String token = null;
                System.out.println("db address: "+db.toString());
                // gets a reference for the remote controller

                // creates and launches the view
                //       new TextView(controller).run();

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


                for (int i = 0; i < 3; i++) {
                        token=null;
                        System.out.print("now try register user:");
                        String username = br.readLine();
                        System.out.print("Enter password:");
                        String password = br.readLine();
                        try {
                                token=db.registerUser(username, password);
                        } catch (CannotRegisterUserException e) {
                                System.out.print(e.getMessage());
                        }
                        if (token != null) {

                                System.out.print("giusto\n");
                                System.out.print("token: " + token + "\n");
                        }else
                                System.out.print("sbagliato\n");

                }
                for (int i = 0; i < 5; i++) {
                        System.out.print("now try login user:");
                        token=null;
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
                                db.addWonGamesFromToken(token);
                                int voti = db.getWonGamesFromToken(token);
                                System.out.println("\nPartite vinte: " + voti);
                        } catch (CannotLoginUserException e) {
                                System.out.print(e.getMessage());
                        }





                }
        }


}