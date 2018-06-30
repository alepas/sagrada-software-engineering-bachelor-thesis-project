package shared.exceptions.gameExceptions;

public class NotYourWpcException extends Exception {
    public final String id;

    public NotYourWpcException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Non puoi selezionare questa wpc (id: " + id
                + ") perchè non ti è stata assegnata";
    }
}
