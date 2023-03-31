package co.empathy.academy.search.common;

import jakarta.json.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentStorage {
    private ConcurrentHashMap <String, List<JsonObject>> jsonMap;

    public DocumentStorage() {
        jsonMap = new ConcurrentHashMap<>();
    }

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
