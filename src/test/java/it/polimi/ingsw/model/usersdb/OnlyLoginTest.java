package it.polimi.ingsw.model.usersdb;



import it.polimi.ingsw.control.RemoteController;
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
        Registry registry = LocateRegistry.getRegistry();
        RemoteController controller = null;
        try {
            controller = (RemoteController) registry.lookup("controller");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));


        System.out.print("now try login user:");
        String username=br.readLine();
        System.out.print("Enter password:");
        String password=br.readLine();
        String token=controller.login(username,password);
        if(token==null)
            System.out.print("sbagliato\n");
        else {
            System.out.print("giusto giumentoso\n");
            System.out.print("token: "+token+"\n");
        }
        System.out.println("ora provo ad aggiungere una partita\n");
        if(controller==null)
            System.out.println("niente db");
        controller.addWonGames(token);
        int voti=controller.getWonGames(token);
        System.out.println("\nPartite vinte: "+voti);
        System.out.print("\nnow try login user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        token=controller.login(username,password);
        if(token==null)
            System.out.print("sbagliato\n");
        else {
            System.out.print("giusto giumentoso\n");
            System.out.print("token: "+token+"\n");
        }
        System.out.println("ora provo ad aggiungere una partita\n");
        controller.addWonGames(token);
        voti=controller.getWonGames(token);
        System.out.println("\nPartite vinte: "+voti);
        System.out.print("\nnow try login user:");
        username=br.readLine();
        System.out.print("Enter password:");
        password=br.readLine();
        token=controller.login(username,password);
        if(token==null)
            System.out.print("sbagliato\n");
        else {
            System.out.print("giusto giumentoso\n");
            System.out.print("token: "+token+"\n");
        }
        System.out.println("ora provo ad aggiungere una partita\n");
        controller.addWonGames(token);
        voti=controller.getWonGames(token);
        System.out.println("\nPartite vinte: "+voti);




    }


}
