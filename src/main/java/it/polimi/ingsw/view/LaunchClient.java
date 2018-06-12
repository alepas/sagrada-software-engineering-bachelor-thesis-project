package it.polimi.ingsw.view;

import it.polimi.ingsw.view.cli.LaunchCli;
import it.polimi.ingsw.view.gui.LaunchGui;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    private static Scanner scanner = new Scanner(System.in);

    private static String userInput(){
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        String answer;
        do {
            System.out.println(">>> Voui usare cli o gui? (Digita \"quit\" per uscire)");
            answer = userInput().toLowerCase();

            if (answer.equals("cli")) {
                try {
                    LaunchCli.main();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (answer.equals("gui")) {
                LaunchGui.main();
            }
        } while (!answer.equals("quit"));
    }


}
