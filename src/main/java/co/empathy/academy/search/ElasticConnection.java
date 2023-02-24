package co.empathy.academy.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.RestClient;



import java.io.IOException;

public class ElasticConnection {

    private static final int defaultPort = 9200;
    private static final ElasticConnection instance = new ElasticConnection(defaultPort);
    private RestClient restClient;
    private ElasticsearchTransport transport;
    private ElasticsearchClient client;
    private ElasticConnection(int port) {
        restClient =RestClient.builder(
                new HttpHost("localhost", port)).build();
        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }

    public static ElasticConnection getInstance() {
        return instance;
    }

    public ElasticsearchClient getClient() {
        return client;
    }

    public String getClusterName() throws ParseException, IOException {
        JSONParser jsonParser = new JSONParser(client.cluster().state().valueBody().toString());

        return jsonParser.parseObject().get("cluster_name").toString();
    }
}
