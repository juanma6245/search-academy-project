package co.empathy.academy.search.model;

import lombok.Data;

@Data
public class SearchResponse {

    private String query;
    private String clusterName;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
