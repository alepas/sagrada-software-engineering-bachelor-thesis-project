package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.LaunchCli;
import it.polimi.ingsw.client.view.gui.LaunchGui;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    private static Scanner scanner = new Scanner(System.in);

    private static String userInput(){
        return scanner.nextLine();
    }

    public static void main(String[] args) {
//        printLogo();

        String answer;
        do {
            System.out.println(">>> Vuoi usare cli o gui? (Digita \"quit\" per uscire)");
            answer = userInput().toLowerCase();

            if (answer.equals("cli") || answer.equals("c")) {
                try {
                    LaunchCli.main();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (answer.equals("gui") || answer.equals("g")) {
                LaunchGui.main();
            }
        } while (!answer.equals("quit") && !answer.equals("q"));
    }

    private static void printLogo() {
        System.out.println( "888888b.                                                         888                 \n" +
                            "888  \"88b                                                        888                 \n" +
                            "888  .88P                                                        888                 \n" +
                            "8888888K.   .d88b.  88888b.  888  888  .d88b.  88888b.  888  888 888888 .d88b.       \n" +
                            "888  \"Y88b d8P  Y8b 888 \"88b 888  888 d8P  Y8b 888 \"88b 888  888 888   d88\"\"88b      \n" +
                            "888    888 88888888 888  888 Y88  88P 88888888 888  888 888  888 888   888  888      \n" +
                            "888   d88P Y8b.     888  888  Y8bd8P  Y8b.     888  888 Y88b 888 Y88b. Y88..88P      \n" +
                            "8888888P\"   \"Y8888  888  888   Y88P    \"Y8888  888  888  \"Y88888  \"Y888 \"Y88P\"       \n" +
                            "                                                                                     \n" +
                            "                                                                                     \n" +
                            "                                                                                     \n" +
                            "d8b                .d8888b.                                         888              \n" +
                            "Y8P               d88P  Y88b                                        888              \n" +
                            "                  Y88b.                                             888              \n" +
                            "888 88888b.        \"Y888b.    8888b.   .d88b.  888d888 8888b.   .d88888  8888b.      \n" +
                            "888 888 \"88b          \"Y88b.     \"88b d88P\"88b 888P\"      \"88b d88\" 888     \"88b     \n" +
                            "888 888  888            \"888 .d888888 888  888 888    .d888888 888  888 .d888888     \n" +
                            "888 888  888      Y88b  d88P 888  888 Y88b 888 888    888  888 Y88b 888 888  888     \n" +
                            "888 888  888       \"Y8888P\"  \"Y888888  \"Y88888 888    \"Y888888  \"Y88888 \"Y888888     \n" +
                            "                                           888                                       \n" +
                            "                                      Y8b d88P                                       \n" +
                            "                                       \"Y88P\"                                        \n");
    }


}
