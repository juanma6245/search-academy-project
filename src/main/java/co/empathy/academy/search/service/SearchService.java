package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.Filter;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.model.ResponseName;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    /**
     * Search for documents in elasticSearch that match the query provided in parameter
     *
     * @param indexName index name
     * @param query     query to search
     * @return list of documents that match the query
     * @throws IOException if the connection to elasticSearch fails
     */
    SearchResponse<ResponseDocument> search(String indexName, String query,int num, int page, List<Filter> filters) throws IOException, NoSearchResultException;

    /**
     * Search for documents in elasticSearch that are trending (high rating)
     * @param indexName index name
     * @param numDocs number of documents to return
     * @param page page number
     * @param filters filters to apply (ex: min number of votes)
     * @return list of documents that are trending
     * @throws IOException if the connection to elasticSearch fails
     * @throws NoSearchResultException if there is no result
     */
    SearchResponse<ResponseDocument> trending(String indexName, int numDocs, int page, List<Filter> filters) throws IOException, NoSearchResultException;

    /**
     * Search for documents in elasticSearch that are similar to the document with the id provided in parameter (similar means same genre and close in startYear)
     * @param indexName index name
     * @param id tconst of the document
     * @param numDocs number of documents to return
     * @param page page number
     * @return list of documents that are similar to the id provided in parameter
     * @throws IOException if the connection to elasticSearch fails
     * @throws NoSearchResultException if there is no result
     */
    SearchResponse<ResponseDocument> similar(String indexName, String id, int numDocs, int page) throws IOException, NoSearchResultException;

    ResponseName searchName(String indexName, String nconst) throws IOException, NoSearchResultException;
}
