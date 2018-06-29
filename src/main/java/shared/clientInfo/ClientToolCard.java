package shared.clientInfo;

import java.io.Serializable;

public class ClientToolCard implements Serializable {
    private final String id;
    private final String name;
    private final String description;
    private final Boolean used;

    public ClientToolCard(String id, String name, String description, Boolean used) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.used = used;
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
}
