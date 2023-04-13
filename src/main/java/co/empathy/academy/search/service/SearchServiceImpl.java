package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.repository.ElasticConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private ElasticConnection elasticConnection;
    @Override
    public List<Hit<ResponseDocument>> search(String indexName, String query) throws IOException, NoSearchResultException {
        List<Hit<ResponseDocument>> result = elasticConnection.search(indexName, query).hits().hits();
        if (result.size() == 0) {
            throw new NoSearchResultException();
        }
        return result;
    }

    @Override
    public Hit<ResponseDocument> searchById(String indexName, String id) {
        return null;
    }
}
