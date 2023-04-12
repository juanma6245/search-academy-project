package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
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
    public List<Hit<ResponseDocument>> search(String indexName, String query) throws IOException {
        return elasticConnection.search(indexName, query).hits().hits();
    }

    @Override
    public SearchResponse searchById(String indexName, String id) {
        return null;
    }
}
