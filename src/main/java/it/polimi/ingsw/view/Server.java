package it.polimi.ingsw.view;

import it.polimi.ingsw.control.*;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.rmi.RemoteException;

/**
 * Hello world!
 *
 */
public class Server
{
    private static DatabaseUsers db =DatabaseUsers.getInstance();
    private Server(){
    }

   /* private static transient DatabaseUsers db;
    public Server() throws IOException { *//*TO BE CHANGED TO PRIVATE*//*
        db=DatabaseUsers.getInstance();
        System.out.println(db);
    }*/

    public static void main(String[] args ) throws IOException {

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        /*System.out.println( "Hello World!" );
        db=DatabaseUsers.getInstance();
        if (db==null)
            System.out.println("noserveredatabase");
            */
/*
        DatabaseUsers valnea = DatabaseUsers.getInstance();
        changedb(valnea);
        if (db==null)
            System.out.println("noserveredatabasedentro Main");*/
        System.out.println("press enter to close:");
        String username=br.readLine();
    }

    private static void changedb(DatabaseUsers my){
        Server.db=my;
    }
    public static DatabaseUsers getDb(){
        if (Server.db==null)
            System.out.println("noserveredatabase");
        return Server.db;
    }





}
