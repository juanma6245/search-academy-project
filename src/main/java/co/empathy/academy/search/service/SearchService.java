package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.ResponseDocument;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    /**
     * Search for documents in elasticSearch that match the query provided in parameter
     * @param indexName index name
     * @param query query to search
     * @return list of documents that match the query
     * @throws IOException if the connection to elasticSearch fails
     */
    List<Hit<ResponseDocument>> search(String indexName, String query) throws IOException, NoSearchResultException;

    /**
     * Search for a document in elasticSearch that match the id provided in parameter
     * @param indexName index name
     * @param id id of the document to search
     * @return the document that match the id
     */
    Hit<ResponseDocument> searchById(String indexName, String id);
}
