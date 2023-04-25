package co.empathy.academy.search.common;

import jakarta.json.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The purpose os this class is to store different List of JsonObjects in a map.
 * This allows the index process to be faster because there is no need to serialize and deserialize the data.
 */
public class DocumentStorage {
    private ConcurrentHashMap <String, List<JsonObject>> jsonMap;

    public DocumentStorage() {
        jsonMap = new ConcurrentHashMap<>();
    }

    /**
     * Stores a list of JsonObjects in a map and returns a key to retrieve the data (key is a random UUID)
     * @param json List of JsonObjects to store
     * @return key to retrieve the data
     */
    public String add(List<JsonObject> json) {
        String key = UUID.randomUUID().toString();
        jsonMap.put(key, json);
        return key;
    }

    public List<JsonObject> get(String key) {
        return jsonMap.get(key);
    }

    public void remove(String key) {
        jsonMap.remove(key);
    }
}
