package co.empathy.academy.search.configuration;




import co.empathy.academy.search.repository.ElasticConnection;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    //private static final String hostname = "elasticsearch";
    private static final String hostname = "localhost";
    private static final int port = 9200;
    @Bean
    public ElasticConnection elasticConnection() {
        return new ElasticConnection(RestClient.builder(new HttpHost(hostname, port)).build());
    }

}
