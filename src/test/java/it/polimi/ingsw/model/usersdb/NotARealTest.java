package it.polimi.ingsw.model.usersdb;



import it.polimi.ingsw.control.RemoteController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class NotARealTest
{
    public static void main(String[] args) throws Exception {
            Registry registry = LocateRegistry.getRegistry();

            // gets a reference for the remote controller
            RemoteController controller = (RemoteController) registry.lookup("controller");

            // creates and launches the view
            //       new TextView(controller).run();

            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter user:");
            String username=br.readLine();
            System.out.print("Enter password:");
            String password=br.readLine();
            controller.registerUser(username,password);
            System.out.print("Enter user:");
            username=br.readLine();
            System.out.print("Enter password:");
            password=br.readLine();
            controller.registerUser(username,password);
            System.out.print("Enter user:");
            username=br.readLine();
            System.out.print("Enter password:");
            password=br.readLine();
            controller.registerUser(username,password);

            System.out.print("now try login user:");
            username=br.readLine();
            System.out.print("Enter password:");
            password=br.readLine();
            String token=controller.login(username,password);
            if(token==null)
                System.out.print("sbagliato\n");
            else {
                System.out.print("giusto giumentoso\n");
                System.out.print("token: "+token+"\n");
            }
            System.out.println("ora provo ad aggiungere una partita\n");
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


    }


}
