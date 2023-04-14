package co.empathy.academy.search.model.title;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Aka implements Title{
    private String titleId;
    private String title;
    private String region;
    private String language;
    private boolean isOriginalTitle;

    public Aka() {
    }
    public Aka(String titleId, int ordering, String title, String region, String language, String[] types, String[] attributes, boolean isOriginalTitle) {
        this.titleId = titleId;
        this.title = title;
        this.region = region;
        this.language = language;
        this.isOriginalTitle = isOriginalTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOriginalTitle() {
        return isOriginalTitle;
    }

    public void setIsOriginalTitle(boolean isOriginalTitle) {
        this.isOriginalTitle = isOriginalTitle;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return titleId;
    }
}
