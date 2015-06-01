package github.turtlehunter.ElCircoServer;

import java.util.HashMap;

public class References {
    HashMap<String, String> references = new HashMap<String, String>();

    public References() {

    }

    public void setReference(String key, String value) {
        references.put(key, value);
    }

    public String getReference(String key) {
        return references.get(key);
    }

    public boolean ReferenceExists(String key) {
        return references.containsKey(key);
    }
}
