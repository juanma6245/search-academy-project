package co.empathy.academy.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.transform.Settings;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import co.empathy.academy.search.model.title.Title;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.elasticsearch.client.RestClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ElasticConnection {


    private RestClient restClient;
    private ElasticsearchTransport transport;
    private ElasticsearchClient client;

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

    public IndexResponse index(String indexName, Title document) throws IOException {
        IndexRequest<Title> request = IndexRequest.of(i -> i
                .index(indexName)
                .id(null)
                .document(document)
        );
        return this.client.index(request);
    }

    public void bulk(String indexName, List<String> jsonString) throws IOException {
        //long init = System.currentTimeMillis();
        BulkRequest.Builder br = new BulkRequest.Builder();
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (String jsonObject : jsonString) {
            jsonObjects.add(Json.createReader(new StringReader(jsonObject)).readObject());
        }
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
        if (response.errors()) {
            System.out.println("Some errors occurred");
            for (BulkResponseItem item : response.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    public boolean deleteIndex(String indexName) throws IOException {
        AcknowledgedResponse response = this.client.indices().delete(i -> i.index(indexName));
        return response.acknowledged();
    }

    public boolean createIndex(String indexName) throws IOException {
        CreateIndexResponse response = this.client.indices().create(createIndexRequest -> createIndexRequest.index(indexName));
        return response.acknowledged();
    }

    public boolean indexExists(String indexName) throws IOException {
        return this.client.indices().exists(request -> request.index(indexName)).value();
    }

    public void setConfig(String indexName, File configFile) throws IOException {
        this.client.indices().close(request -> request.index(indexName));

        FileInputStream fileInputStream = new FileInputStream(configFile);

        this.client.indices().putSettings(request -> request.withJson(fileInputStream));

        this.client.indices().open(request -> request.index(indexName));
    }

    public void setMapping(String indexName, File mappingFile) throws IOException {
        FileInputStream mappingInputStream = new FileInputStream(mappingFile);
        this.client.indices().putMapping(request -> request.index(indexName).withJson(mappingInputStream));
    }
}
