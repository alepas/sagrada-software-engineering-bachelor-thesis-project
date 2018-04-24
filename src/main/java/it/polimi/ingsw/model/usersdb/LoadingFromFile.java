package it.polimi.ingsw.model.usersdb;

import java.io.*;


class LoadingFromFile {

    private LoadingFromFile() {
    }

    static Object fromFile(String name) throws FileNotFoundException{
        Object output = null;
        FileInputStream fis = null;
        ObjectInputStream ois=null;

        try {
            fis = new FileInputStream(name);


            try {
                ois = new ObjectInputStream(fis);
                output = ois.readObject();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return null;
            }
            finally {
                if (ois!=null)
                ois.close();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
