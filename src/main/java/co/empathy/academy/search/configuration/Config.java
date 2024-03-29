package co.empathy.academy.search.configuration;

import co.empathy.academy.search.common.DocumentStorage;
import co.empathy.academy.search.repository.ElasticConnection;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class Config implements AsyncConfigurer {

    private static final String elasticsearch = "elasticsearch";
    private static final String hostname = "localhost";
    private static final int port = 9200;



    @Bean
    public ElasticConnection elasticConnection() {
        return new ElasticConnection(RestClient.builder(new HttpHost(hostname, port), new HttpHost(elasticsearch, port)).build());
    }
    @Bean
    public DocumentStorage documentStorage() {
        return new DocumentStorage();
    }

    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        //MongoDBStorageProvider storageProvider = new MongoDBStorageProvider(mongoClient);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

}
