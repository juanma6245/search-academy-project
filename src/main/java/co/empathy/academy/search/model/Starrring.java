package co.empathy.academy.search.model;

public class Starrring {
    private Name name;
    private String characters;

    public Starrring() {
    }
    public Starrring(Name name, String characters) {
        this.name = name;
        this.characters = characters;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }
}
