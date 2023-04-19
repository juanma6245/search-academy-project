package co.empathy.academy.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.common.DocumentStorage;
import co.empathy.academy.search.model.ResponseDocument;
import co.empathy.academy.search.model.title.Title;
import jakarta.json.JsonObject;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Class to connect to elasticSearch.
 */
public class ElasticConnection {


    private RestClient restClient;
    private ElasticsearchTransport transport;
    private ElasticsearchClient client;

    @Autowired
    private DocumentStorage documentStorage;
    private CompletionSuggest.Builder suggestBuilder;

    public ElasticConnection(RestClient restClient) {
        //restClient =RestClient.builder(
        //        new HttpHost(hostname, port)).build();
        this.restClient = restClient;
        this.transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        this.client = new ElasticsearchClient(this.transport);
    }



    public ElasticsearchClient getClient() {
        return client;
    }

    public String getClusterName() throws IOException {

        return this.client.cluster().health().clusterName();
    }

    /**
     * Index a document into an index in elasticSearch
     * @param indexName name of the index
     * @param document document to index
     * @return response of the index request
     * @throws IOException if the request fails
     */
    public IndexResponse index(String indexName, Title document) throws IOException {
        IndexRequest<Title> request = IndexRequest.of(i -> i
                .index(indexName)
                .id(null)
                .document(document)
        );
        return this.client.index(request);
    }

    /**
     * Index a list of documents into an index in elasticSearch. Documents are indexed in bulk and must be in json format
     * @param indexName name of the index
     * @param key identifier of the list of documents in documentStorage
     * @throws IOException if the request fails
     */
    public void bulk(String indexName, String key) throws IOException {
        //long init = System.currentTimeMillis();
        BulkRequest.Builder br = new BulkRequest.Builder();
        List<JsonObject> jsonObjects = this.documentStorage.get(key);
        System.out.println(jsonObjects.get(0).getString("tconst"));
        for (JsonObject jsonObject : jsonObjects) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .document(jsonObject)
                    )
            );
        }
        BulkResponse response = this.client.bulk(br.build());
        //long end = System.currentTimeMillis();
        //System.out.println("Time elastic: " + (end - init) + "ms");
        this.documentStorage.remove(key);
        if (response.errors()) {
            System.out.println("Some errors occurred");
            for (BulkResponseItem item : response.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    /**
     * Delete an index in elasticSearch
     * @param indexName name of the index
     * @return true if the index was deleted, false otherwise
     * @throws IOException if the request fails
     */
    public boolean deleteIndex(String indexName) throws IOException {
        AcknowledgedResponse response = this.client.indices().delete(i -> i.index(indexName));
        return response.acknowledged();
    }

    /**
     * Create an index in elasticSearch
     * @param indexName name of the index
     * @return true if the index was created, false otherwise
     * @throws IOException if the request fails
     */
    public boolean createIndex(String indexName) throws IOException {
        CreateIndexResponse response = this.client.indices().create(createIndexRequest -> createIndexRequest.index(indexName));
        return response.acknowledged();
    }

    /**
     * Check if an index exists in elasticSearch
     * @param indexName name of the index
     * @return true if the index exists, false otherwise
     * @throws IOException if the request fails
     */
    public boolean indexExists(String indexName) throws IOException {
        return this.client.indices().exists(request -> request.index(indexName)).value();
    }

    /**
     * Set the configuration of an index in elasticSearch
     * @param indexName name of the index
     * @param configFile file with the configuration to set
     * @throws IOException if the request fails
     */
    public void setConfig(String indexName, File configFile) throws IOException {
        this.client.indices().close(request -> request.index(indexName));

        FileInputStream fileInputStream = new FileInputStream(configFile);

        this.client.indices().putSettings(request -> request.index(indexName).withJson(fileInputStream));

        this.client.indices().open(request -> request.index(indexName));
    }

    /**
     * Set the mapping of an index in elasticSearch
     * @param indexName name of the index
     * @param mappingFile file with the mapping to set
     * @throws IOException if the request fails
     */
    public void setMapping(String indexName, File mappingFile) throws IOException {
        FileInputStream mappingInputStream = new FileInputStream(mappingFile);
        this.client.indices().putMapping(request -> request.index(indexName).withJson(mappingInputStream));
    }

    public SearchResponse search(String indexName, String query,int size, int from, BoolQuery.Builder filter) throws IOException {
        List<Query> listQuery = new ArrayList<>();
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        MultiMatchQuery nameQuery = MultiMatchQuery.of(q -> q
                .query(query)
                .fields("primaryTitle", "originalTitle")
                .type(TextQueryType.BestFields)
        );

        listQuery.add(nameQuery._toQuery());
        listQuery.add(filter.build()._toQuery());
        boolQuery.must(listQuery);//If not must the filtering process changes the order of the results
        Map<String, Aggregation> aggregationMap = this._getBasicsAggregations();
        SearchRequest.Builder request = new SearchRequest.Builder().index(indexName);


        request.query(boolQuery.build()._toQuery());
        request.aggregations(aggregationMap);
        request.size(size);
        request.from(from);

        SearchResponse<ResponseDocument> response =this.client.search(request.build(), ResponseDocument.class);
        System.out.println("Total results: " + response.hits().total().value());
        if (response.hits().total().value() == 0) {
            System.out.println("No results found with the name query, trying with the prefix query");
            PrefixQuery.Builder prefixBuilder = QueryBuilders.prefix().field("primaryTitle").value(query);

            request = new SearchRequest.Builder().index(indexName);
            request.query(prefixBuilder.build()._toQuery());
            request.aggregations(aggregationMap);
            request.size(size);
            request.from(from);
            System.out.println("Request");
            response = this.client.search(request.build(), ResponseDocument.class);
        }
        return response;
    }

    private Map<String, Aggregation> _getBasicsAggregations() {
        Aggregation aggregationMinRating = Aggregation.of(a -> a
                .min(m -> m
                        .field("averageRating")
                )
        );
        Aggregation aggregationMaxRating = Aggregation.of(a -> a
                .max(m -> m
                        .field("averageRating")
                )
        );
        Aggregation aggregationGenres = Aggregation.of(a -> a
                .terms(t -> t
                        .field("genres")
                )
        );
        /*Aggregation directorsNconst = Aggregation.of(a -> a
                .terms(t -> t
                        .field("directors.nconst")
                )
        );
        Aggregation aggregationDirectors = Aggregation.of(a -> a
                .nested(n -> n
                        .path("directors"))
                .aggregations(directorsNconst.aggregations())
        );*/

        Aggregation aggregationMinReleaseYear = Aggregation.of(a -> a
                .min(m -> m
                        .field("startYear")
                )
        );
        Aggregation aggregationMaxReleaseYear = Aggregation.of(a -> a
                .max(m -> m
                        .field("startYear")
                )
        );
        Aggregation aggregationMinDuration = Aggregation.of(a -> a
                .min(m -> m
                        .field("runtimeMinutes")
                )
        );
        Aggregation aggregationMaxDuration = Aggregation.of(a -> a
                .max(m -> m
                        .field("runtimeMinutes")
                )
        );

        Map<String, Aggregation> aggregationMap = new HashMap<>();
        aggregationMap.put("minRating", aggregationMinRating);
        aggregationMap.put("maxRating", aggregationMaxRating);
        aggregationMap.put("genres", aggregationGenres);
        //aggregationMap.put("directors", aggregationDirectors);
        aggregationMap.put("minReleaseYear", aggregationMinReleaseYear);
        aggregationMap.put("maxReleaseYear", aggregationMaxReleaseYear);
        aggregationMap.put("minDuration", aggregationMinDuration);
        aggregationMap.put("maxDuration", aggregationMaxDuration);

        return aggregationMap;
    }

    public SearchResponse<ResponseDocument> getAggregations(String indexName) throws IOException {
        Map<String, Aggregation> aggregationMap = this._getBasicsAggregations();
        SearchRequest.Builder request = new SearchRequest.Builder().index(indexName);
        request.aggregations(aggregationMap);
        request.size(0);

        SearchResponse<ResponseDocument> response = this.client.search(request.build(), ResponseDocument.class);
        return response;
    }

    public SearchResponse<ResponseDocument> trending(String indexName, int num, int page, BoolQuery.Builder filter) throws IOException {
        SearchRequest.Builder request = new SearchRequest.Builder().index(indexName);
        request.query(filter.build()._toQuery());
        request.sort(s -> s
                .field(FieldSort.of(f -> f
                        .field("averageRating")
                        .order(SortOrder.Desc)
                )));
        SearchResponse<ResponseDocument> response = this.client.search(request.build(), ResponseDocument.class);
        return response;
    }
}
