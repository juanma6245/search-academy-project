package co.empathy.academy.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.Title;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

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

    public IndexResponse index(Title document, TSVtype type) throws IOException {
        IndexRequest<Title> request = IndexRequest.of(i -> i
                .index(type.toString().toLowerCase())
                .id(null)
                .document(document)
        );
        return this.client.index(request);
    }
}
