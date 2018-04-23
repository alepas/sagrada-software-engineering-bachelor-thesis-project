package it.polimi.ingsw.model.usersdb;

import java.io.*;


public class LoadingFromFile {

    public static Object fromFile(String name) throws FileNotFoundException {
        Object output = null;

            FileInputStream fis = new FileInputStream(name);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            output = ois.readObject();
            ois.close();
            fis.close();
      } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return output;
    }

    public static void toFile(Object root, String name) {

        try {
            FileOutputStream fos = new FileOutputStream(name);
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(fos);
            oos.writeObject(root);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
