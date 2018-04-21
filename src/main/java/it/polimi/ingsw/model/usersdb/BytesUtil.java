package it.polimi.ingsw.model.usersdb;

import java.io.*;

public class BytesUtil {

    // toByteArray and toObject are taken from: http://tinyurl.com/69h8l7x
    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }


    public static UserInDB toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        UserInDB obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream in = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bis);
            obj = (UserInDB)in.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return obj;
    }

    public static String toString(byte[] bytes) {
        return new String(bytes);
    }
}
