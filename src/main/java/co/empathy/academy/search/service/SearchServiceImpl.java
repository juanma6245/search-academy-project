package co.empathy.academy.search.service;

import co.empathy.academy.search.service.client.SearchEngine;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService{

    private final SearchEngine searchEngine;

    public SearchServiceImpl(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    @Override
    public String search(String query) throws ParseException, IOException {
        String response;
        if (!query.isBlank()) {
            response = this.searchEngine.executeQuery(query);
        } else {
            response = "Error: Query is blank";
        }
        return response;
    }

    @Override
    public String getClusterName() throws ParseException, IOException {
        return searchEngine.executeQuery("queryCluster");
    }
}
