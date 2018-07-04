package shared.exceptions.gameExceptions;

public class InvalidGameParametersException extends Exception {
    private final int number;
    private final boolean playersOrLevel;

    public InvalidGameParametersException(int number, boolean playersOrLevel){
        this.number=number;
        this.playersOrLevel=playersOrLevel;
    }

    @Override
    public String getMessage() {
        if (playersOrLevel)
        return "Cannot find a game with this amount of players: " + number;
        else
            return "Cannot find a game with this level: " + number;

    }
}