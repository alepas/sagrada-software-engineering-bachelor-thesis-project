package it.polimi.ingsw.model;
import java.io.FileReader;

public class WPC {
    private int favour;
    private Cell[][] schema;
    /*public static void readFile() {

        String path = "C:/html.txt";
        char[] in = new char[50];
        int size = 0;
        try {
            file file = new file(path);
            FileReader fr = new FileReader(file);
            size = fr.read(in);

            System.out.print("Caratteri presenti: " + size + "\n");
            System.out.print("Il contenuto del file è il seguente:\n");

            for(int i=0; i<size; i++)
                System.out.print(in[i]);
            fr.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }*/
    private void checkCellRestriction(){}
    private void checkAdjacentRestriction(){}
}
