package it.polimi.ingsw.model.usersdb;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.WPC;

public class PlayerInGame {
    private User user;
    private Colour privateObjective1;
    private WPC wpc;

    public PlayerInGame(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Colour getPrivateObjective1() {
        return privateObjective1;
    }

    public WPC getWpc() {
        return wpc;
    }
}
