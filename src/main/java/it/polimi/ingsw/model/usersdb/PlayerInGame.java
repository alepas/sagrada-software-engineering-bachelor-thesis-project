package it.polimi.ingsw.model.usersdb;

public class PlayerInGame {
    private User user;

    public PlayerInGame(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
