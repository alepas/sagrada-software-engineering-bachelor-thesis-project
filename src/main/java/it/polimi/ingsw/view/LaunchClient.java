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
        System.out.println("                                                                                                                                 \n" +
                "                                                                                                       dddddddd                  \n" +
                "   SSSSSSSSSSSSSSS                                                                                     d::::::d                  \n" +
                " SS:::::::::::::::S                                                                                    d::::::d                  \n" +
                "S:::::SSSSSS::::::S                                                                                    d::::::d                  \n" +
                "S:::::S     SSSSSSS                                                                                    d:::::d                   \n" +
                "S:::::S              aaaaaaaaaaaaa     ggggggggg   gggggrrrrr   rrrrrrrrr   aaaaaaaaaaaaa      ddddddddd:::::d   aaaaaaaaaaaaa   \n" +
                "S:::::S              a::::::::::::a   g:::::::::ggg::::gr::::rrr:::::::::r  a::::::::::::a   dd::::::::::::::d   a::::::::::::a  \n" +
                " S::::SSSS           aaaaaaaaa:::::a g:::::::::::::::::gr:::::::::::::::::r aaaaaaaaa:::::a d::::::::::::::::d   aaaaaaaaa:::::a \n" +
                "  SS::::::SSSSS               a::::ag::::::ggggg::::::ggrr::::::rrrrr::::::r         a::::ad:::::::ddddd:::::d            a::::a \n" +
                "    SSS::::::::SS      aaaaaaa:::::ag:::::g     g:::::g  r:::::r     r:::::r  aaaaaaa:::::ad::::::d    d:::::d     aaaaaaa:::::a \n" +
                "       SSSSSS::::S   aa::::::::::::ag:::::g     g:::::g  r:::::r     rrrrrrraa::::::::::::ad:::::d     d:::::d   aa::::::::::::a \n" +
                "            S:::::S a::::aaaa::::::ag:::::g     g:::::g  r:::::r           a::::aaaa::::::ad:::::d     d:::::d  a::::aaaa::::::a \n" +
                "            S:::::Sa::::a    a:::::ag::::::g    g:::::g  r:::::r          a::::a    a:::::ad:::::d     d:::::d a::::a    a:::::a \n" +
                "SSSSSSS     S:::::Sa::::a    a:::::ag:::::::ggggg:::::g  r:::::r          a::::a    a:::::ad::::::ddddd::::::dda::::a    a:::::a \n" +
                "S::::::SSSSSS:::::Sa:::::aaaa::::::a g::::::::::::::::g  r:::::r          a:::::aaaa::::::a d:::::::::::::::::da:::::aaaa::::::a \n" +
                "S:::::::::::::::SS  a::::::::::aa:::a gg::::::::::::::g  r:::::r           a::::::::::aa:::a d:::::::::ddd::::d a::::::::::aa:::a\n" +
                " SSSSSSSSSSSSSSS     aaaaaaaaaa  aaaa   gggggggg::::::g  rrrrrrr            aaaaaaaaaa  aaaa  ddddddddd   ddddd  aaaaaaaaaa  aaaa\n" +
                "                                                g:::::g                                                                          \n" +
                "                                    gggggg      g:::::g                                                                          \n" +
                "                                    g:::::gg   gg:::::g                                                                          \n" +
                "                                     g::::::ggg:::::::g                                                                          \n" +
                "                                      gg:::::::::::::g                                                                           \n" +
                "                                        ggg::::::ggg                                                                             \n" +
                "                                           gggggg                                                                                \n");

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
