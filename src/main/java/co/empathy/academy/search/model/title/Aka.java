package co.empathy.academy.search.model.title;

public class Aka implements Title{
    private String titleId;
    private int ordering;
    private String title;
    private String region;
    private String language;
    private String[] types;
    private String[] attributes;
    private String isOriginalTitle;

    public Aka(String titleId, int ordering, String title, String region, String language, String[] types, String[] attributes, String isOriginalTitle) {
        this.titleId = titleId;
        this.ordering = ordering;
        this.title = title;
        this.region = region;
        this.language = language;
        this.types = types;
        this.attributes = attributes;
        this.isOriginalTitle = isOriginalTitle;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
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

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public String getIsOriginalTitle() {
        return isOriginalTitle;
    }

    public void setIsOriginalTitle(String isOriginalTitle) {
        this.isOriginalTitle = isOriginalTitle;
    }
}
