package shared.exceptions.usersAndDatabaseExceptions;

public class NoToolCardInUseException extends Exception{
    private String user;


    public NoToolCardInUseException(String username) {
        this.user=username;

    }
    @Override
    public String getMessage() {
        return "No ToolCard in setCard";
    }



}
