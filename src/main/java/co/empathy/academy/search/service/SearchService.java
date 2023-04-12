package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.model.ResponseDocument;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<Hit<ResponseDocument>> search(String indexName, String query) throws IOException;
    SearchResponse searchById(String indexName, String id);
}
