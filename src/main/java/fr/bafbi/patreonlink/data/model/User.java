package fr.bafbi.patreonlink.data.model;

import fr.bafbi.patreonlink.patreon.PatreonTokens;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "users", schemaVersion = "1.0")
public class User {

    @Id
    private String uuid;

    private PatreonTokens tokens;

    public User() {
    }

    public User(String uniqueId, PatreonTokens tokens) {
        this.uuid = uniqueId;
        this.tokens = tokens;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public PatreonTokens getTokens() {
        return tokens;
    }

    public void setTokens(PatreonTokens tokens) {
        this.tokens = tokens;
    }

}
