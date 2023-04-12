package co.empathy.academy.search.common;

import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStorageTest {

    private DocumentStorage documentStorage;
    private List<JsonObject> json;
    private String key;

    @BeforeEach
    void setUp() {
        documentStorage = new DocumentStorage();
        json = new ArrayList<>();
        json.add(JsonObject.EMPTY_JSON_OBJECT);
    }

    @Test
    public void whenAdd_thenDocumentAdded() {
        key = documentStorage.add(json);
        assertNotNull(key);
        assertEquals(json, documentStorage.get(key));
    }

    @Test
    public void whenGet_thenNullIfKeyNotFound() {
        assertNull(documentStorage.get("notFound"));
    }

    @Test
    public void whenRemove_thenElementIsRemoved() {
        key = documentStorage.add(json);
        documentStorage.remove(key);
        assertNull(documentStorage.get(key));
    }
}