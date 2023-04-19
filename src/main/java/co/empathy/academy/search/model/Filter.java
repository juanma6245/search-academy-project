package co.empathy.academy.search.model;

public class Filter {
     public enum TYPE {
         TERM,
         MIN,
         MAX,
        GENRE
    }
    private TYPE type;
    private String key;
    private String value;

    public Filter(TYPE type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
