package it.polimi.ingsw.model.usersdb;

import java.io.*;
import java.util.HashMap;


class LoadingFromFile {

    private LoadingFromFile() {
    }

    static Object fromFile(String name) throws FileNotFoundException {
        Object output = null;
        FileInputStream fis = null;
        ObjectInputStream ois=null;

        fis = new FileInputStream(name);
        try {
            ois = new ObjectInputStream(fis);
                try {
                    output = ois.readObject();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                finally{
                    ois.close();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return output;
    }

    public static void toFile(Object root, String name) {

        Object output = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(name);
            try {
                oos = new ObjectOutputStream(fos);
                oos.writeObject(root);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                oos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



/*
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
    }*/
}
