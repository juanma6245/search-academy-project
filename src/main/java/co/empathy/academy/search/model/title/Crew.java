package co.empathy.academy.search.model.title;

public class Crew implements Title{
    private String tconst;
    private String[] directors;
    private String[] writers;

    public Crew(String tconst, String[] directors, String[] writers) {
        this.tconst = tconst;
        this.directors = directors;
        this.writers = writers;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String[] getDirectors() {
        return directors;
    }

    public void setDirectors(String[] directors) {
        this.directors = directors;
    }

    public String[] getWriters() {
        return writers;
    }

    public void setWriters(String[] writers) {
        this.writers = writers;
    }
}
