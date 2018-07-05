package shared.clientInfo;

import java.io.Serializable;

public class ClientToolCard implements Serializable {
    private final String id;
    private String name;
    private String description;
    private final Boolean used;
    private final ClientColor color;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
