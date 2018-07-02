package shared.configLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLoader {

    public static void createDir(String path) {
        new File(path).mkdirs();
    }


     public static void setFile(String newPath, String oldPathWithFileName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        File outFile;
         Path p = Paths.get(oldPathWithFileName);
         String filename = p.getFileName().toString();
         if (new File(newPath+"/"+filename).isFile()) {
             return;
         }
        try {
            stream = FileLoader.class.getResourceAsStream(oldPathWithFileName);
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + oldPathWithFileName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];

            outFile = new File(newPath+"/"+filename);
            resStreamOut = new FileOutputStream(outFile);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        }finally{
             resStreamOut.close();
             stream.close();
        }

        return;
    }



}
