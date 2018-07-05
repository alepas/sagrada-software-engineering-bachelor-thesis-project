package client;

import client.view.cli.LaunchCli;
import client.view.gui.LaunchGui;
import static client.constants.CliConstants.*;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    private static Scanner scanner = new Scanner(System.in);

    private static String userInput(){
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        System.out.println("  ____                            _       \n" +
                " / ___|  __ _  __ _ _ __ __ _  __| | __ _ \n" +
                " \\___ \\ / _` |/ _` | '__/ _` |/ _` |/ _` |\n" +
                "  ___) | (_| | (_| | | | (_| | (_| | (_| |\n" +
                " |____/ \\__,_|\\__, |_|  \\__,_|\\__,_|\\__,_|\n" +
                "              |___/                       \n");

        String answer;
        do {
            System.out.println(CLI_OR_GUI);
            answer = userInput().toLowerCase();

            if (CLI_RESPONSES.contains(answer)) {
                try {
                    LaunchCli.main();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (GUI_RESPONSES.contains(answer)) {
                LaunchGui.main();
            }
        } while (!QUIT_RESPONSES.contains(answer));
    }
}
