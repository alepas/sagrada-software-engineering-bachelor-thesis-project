package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public class ClientPoc implements Serializable {
    private final String id;
    private final String name;
    private final String description;

    public ClientPoc(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
