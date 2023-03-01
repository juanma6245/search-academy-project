package co.empathy.academy.search.controller;

import co.empathy.academy.search.model.SearchResponse;
import co.empathy.academy.search.service.SearchService;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @GetMapping("/search{query}")
    public ResponseEntity<SearchResponse> search(@RequestParam(name = "query", defaultValue = "No query") String query) {

        SearchResponse response = new SearchResponse();
        String clusterName = null;

        try {
            clusterName = searchService.getClusterName();
        } catch (IOException e) {
            clusterName = "Error retrieving cluster name: " + e.getMessage();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        response.setQuery(query);

        response.setClusterName(clusterName);
        return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
    }
}
