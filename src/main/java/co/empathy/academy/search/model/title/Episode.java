package co.empathy.academy.search.model.title;

public class Episode implements Title{
    private String tconst;
    private String parentTconst;
    private int seasonNumber;
    private int episodeNumber;

    public Episode(String tconst, String parentTconst, int seasonNumber, int episodeNumber) {
        this.tconst = tconst;
        this.parentTconst = parentTconst;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getParentTconst() {
        return parentTconst;
    }

    public void setParentTconst(String parentTconst) {
        this.parentTconst = parentTconst;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
