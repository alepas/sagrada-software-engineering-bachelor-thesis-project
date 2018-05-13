package it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions;

public class DatabaseFileErrorException extends Exception{
    private int cause;

    public DatabaseFileErrorException(int errorID) {
        cause=errorID;
    }
    @Override
    public String getMessage() {
        switch (cause){
            case 0: return "There has been an internal problem in the database.";
            case 1: return "There has been an internal problem finding the database.";
        }
        return "There has been an internal problem in the database.";
    }
    public int getErrorId() {
        return cause;
    }


}
