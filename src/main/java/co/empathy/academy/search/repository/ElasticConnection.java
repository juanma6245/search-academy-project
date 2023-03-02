package co.empathy.academy.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
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

    public String getClusterName() throws ParseException, IOException {
        JSONParser jsonParser = new JSONParser(client.cluster().state().valueBody().toString());

        return jsonParser.parseObject().get("cluster_name").toString();
    }
}
