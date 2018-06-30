package shared.exceptions.wpcExceptions;

public class InvalidWpcIdException extends Exception {
    public final String id;

    public InvalidWpcIdException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Non esiste una Wpc con questo id:" + id;
    }
}
