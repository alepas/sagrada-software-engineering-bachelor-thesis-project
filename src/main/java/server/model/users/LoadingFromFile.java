package server.model.users;

import shared.exceptions.usersAndDatabaseExceptions.DatabaseFileErrorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class LoadingFromFile {

    private LoadingFromFile() {
    }

    static Object fromFile(String name) throws DatabaseFileErrorException {
        Object output;
        Path pathToFile = Paths.get(name);
        try {
            Files.createDirectories(pathToFile.getParent());
        } catch (IOException e) {
            throw new DatabaseFileErrorException(1);
        }

        try (InputStream fis = new FileInputStream(name);
                ObjectInputStream ois=new ObjectInputStream(fis);
                ) {
            output = ois.readObject();

        } catch (FileNotFoundException e) {
            throw new DatabaseFileErrorException(1);
        } catch (IOException | ClassNotFoundException e) {
            throw new DatabaseFileErrorException(0);
        }


        return output;
    }

    static void toFile(Object root, String name) throws DatabaseFileErrorException {
        Path pathToFile = Paths.get(name);
        try {
            Files.createDirectories(pathToFile.getParent());
        } catch (IOException e) {
            throw new DatabaseFileErrorException(1);
        }
        try (
                FileOutputStream fos = new FileOutputStream(name);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        )
        {
            oos.writeObject(root);
        }

        catch (FileNotFoundException e) {
            throw new DatabaseFileErrorException(1);
        }
        catch (IOException e) {
            throw new DatabaseFileErrorException(0);}

    }


}
