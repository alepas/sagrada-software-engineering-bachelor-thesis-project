package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the toolcard object in the server model, it doesn't contains any logic
 */
public class ClientToolCard implements Serializable {
    private final String id;
    private String name;
    private String description;
    private final Boolean used;
    private final ClientColor color;

    /**
     * Constructor of this.
     *
     * @param id is the toolcard id
     * @param name is the toolcard name
     * @param description is the toolcard description
     * @param color is the color that a dice must have to activated the toolcard
     * @param used is true if the toolcard has already been used
     */
    public ClientToolCard(String id, String name, String description, ClientColor color, Boolean used) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.used = used;
        this.color=color;
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

    public Boolean getUsed() {
        return used;
    }

    public ClientColor getColor(){ return color;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
