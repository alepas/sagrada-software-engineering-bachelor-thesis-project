package shared.configLoader;

import shared.exceptions.loadConfig.ConfigFileErrorException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLoader {

    private FileLoader(){}

    public static void createDir(String path) {
        new File(path).mkdirs();
    }


     public static void setFile(String newPath, String oldPathWithFileName) throws ConfigFileErrorException {
         File outFile;
         Path p = Paths.get(oldPathWithFileName);
         String filename = p.getFileName().toString();
         if (new File(newPath+"/"+filename).isFile()) {
             return;
         }
         int readBytes;
         byte[] buffer = new byte[4096];
         outFile = new File(newPath+"/"+filename);

         try (InputStream stream = FileLoader.class.getResourceAsStream(oldPathWithFileName); OutputStream resStreamOut = new FileOutputStream(outFile)) {
             if(stream == null) {
                 throw new ConfigFileErrorException(1);
             }
             while ((readBytes = stream.read(buffer)) > 0) {
                 resStreamOut.write(buffer, 0, readBytes);
             }
         } catch (IOException ex) {
             throw new ConfigFileErrorException(0);
         }

    }



}
