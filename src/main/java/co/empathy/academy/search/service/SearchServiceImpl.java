package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.repository.ElasticConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private ElasticConnection elasticConnection;
    @Override
    public SearchResponse<ResponseDocument> search(String indexName, String query) throws IOException, NoSearchResultException {
        SearchResponse<ResponseDocument> result = elasticConnection.search(indexName, query);
        if (result.hits().hits().size() == 0) {
            throw new NoSearchResultException();
        }
        return result;
    }

    @Override
    public Hit<ResponseDocument> searchById(String indexName, String id) {
        return null;
    }
}
