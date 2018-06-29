package shared.clientInfo;

import java.io.Serializable;

public class ClientUser implements Serializable {
    private final String username;
    private final int wonGames;
    private final int lostGames;
    private final int abandonedGames;
    private final int ranking;

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
