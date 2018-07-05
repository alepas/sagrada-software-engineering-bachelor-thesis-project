package shared.clientinfo;

import java.io.Serializable;

/**
 * It's a copy of the public card object in the server model, it doesn't contains any logic
 */
public class ClientPoc implements Serializable {
    private final String id;
    private final String name;
    private final String description;

    /**
     * @param id is th epublic object id
     * @param name is the poc name
     * @param description is the description of what must be done to obtain the score given by the card
     */
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
