package server.model.users;

import shared.exceptions.usersAndDatabaseExceptions.DatabaseFileErrorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * This class contains the methods for the serialization of the user database and the storing of it in an external file.
 */
class FileObjectSerialization {

    /**
     * private constructor.
     */
    private FileObjectSerialization() {
    }


    /**
     * Loads the serialized object from the file, deserializes it and returns it to the caller method.
     *
     * @param fileName is the name in the form [path/name] of the file that contains the object.
     * @return the Object deserialized.
     * @throws DatabaseFileErrorException if the file cannot be found or if there was a problem in the file.
     */
    static Object fromFile(String fileName) throws DatabaseFileErrorException {
        Object output;
        Path pathToFile = Paths.get(fileName);
        try {
            Files.createDirectories(pathToFile.getParent());
        } catch (IOException e) {
            throw new DatabaseFileErrorException(1);
        }

        try (InputStream fis = new FileInputStream(fileName);
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


    /**
     * Stores the passed object in the file specified by the name and path.
     * If there is no file present, the method does not create a new one.
     *
     * @param objectToSerialize is the object that needs to be saved.
     * @param fileName is the name in the form [path/name] of the file that will contain the object.
     * @throws DatabaseFileErrorException if the file cannot be found or if there was a problem in the saving process.
     */
    static void toFile(Object objectToSerialize, String fileName) throws DatabaseFileErrorException {
        Path pathToFile = Paths.get(fileName);
        try {
            Files.createDirectories(pathToFile.getParent());
        } catch (IOException e) {
            throw new DatabaseFileErrorException(1);
        }
        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        )
        {
            oos.writeObject(objectToSerialize);
        }

        catch (FileNotFoundException e) {
            throw new DatabaseFileErrorException(1);
        }
        catch (IOException e) {
            throw new DatabaseFileErrorException(0);}

    }


}
