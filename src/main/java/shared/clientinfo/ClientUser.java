package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the user object in the server model, it doesn't contains any logic. This class in used
 * to update the personal area of each player
 */
public class ClientUser implements Serializable {
    private final String username;
    private final int wonGames;
    private final int lostGames;
    private final int abandonedGames;
    private final int ranking;

    /**
     * @param username is the player's username
     * @param wonGames is the number of won games
     * @param lostGames is the number of lost games
     * @param abandonedGames is the number of abamdoned games
     * @param ranking is the player ranking
     */
    public ClientUser(String username, int wonGames, int lostGames, int abandonedGames, int ranking) {
        this.username = username;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.abandonedGames = abandonedGames;
        this.ranking = ranking;
    }

    public ClientUser(String username) {
        this(username, 0, 0, 0, 0);
    }

    public String getUsername() {
        return username;
    }

    public int getWonGames() {
        return wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public int getAbandonedGames() {
        return abandonedGames;
    }

    public int getRanking() {
        return ranking;
    }
}
