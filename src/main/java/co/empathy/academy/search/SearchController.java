package co.empathy.academy.search;


import co.empathy.academy.ElasticConnection;
import org.apache.http.HttpHost;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.RestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class SearchController {

    @GetMapping("/search{query}")
    public ResponseEntity<SearchResponse> search(@RequestParam(name = "query", defaultValue = "No query") String query) {

        SearchResponse response = new SearchResponse();
        String clusterName = null;
        String hostname = "elasticsearch";
        int port = 9200;
        ElasticConnection connection = new ElasticConnection(RestClient.builder(new HttpHost(hostname, port)).build());
        try {
            clusterName = connection.getClusterName();
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
