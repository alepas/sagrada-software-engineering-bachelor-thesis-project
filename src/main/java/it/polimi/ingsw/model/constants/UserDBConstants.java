package it.polimi.ingsw.model.constants;

public class UserDBConstants {
    private UserDBConstants(){}

    public static final String PATH_DB_FILE = "src/main/resources/database/store.db";

    public static String getPathDbFile() {
        return PATH_DB_FILE;
    }
}
