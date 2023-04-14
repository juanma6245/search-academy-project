package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.empathy.academy.search.exception.NoSearchResultException;
import co.empathy.academy.search.model.Filter;
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
    public SearchResponse<ResponseDocument> search(String indexName, String query, List<Filter> filters) throws IOException, NoSearchResultException {
        BoolQuery.Builder filter = this._buildFilter(filters);
        SearchResponse<ResponseDocument> result = elasticConnection.search(indexName, query, filter);
        if (result.hits().hits().size() == 0) {
            throw new NoSearchResultException();
        }
        return result;
    }

    private BoolQuery.Builder _buildFilter(List<Filter> filters) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        for (Filter filter : filters) {
            switch (filter.getType()) {
                case RANGE:
                    boolQuery.filter(f -> f
                            .range(r -> r
                                    .field(filter.getKey())
                                    .gte(JsonData.fromJson(filter.getValue()))
                                    .boost(0.0F)
                            ));
                    break;
                case TERM:
                    boolQuery.filter(f -> f
                            .term(t -> t
                                    .field(filter.getKey())
                                    .value(filter.getValue())
                            ));
                    break;
                default:
                    break;
            }
        }

        return boolQuery;
    }

    @Override
    public Hit<ResponseDocument> searchById(String indexName, String id) {
        return null;
    }
}
