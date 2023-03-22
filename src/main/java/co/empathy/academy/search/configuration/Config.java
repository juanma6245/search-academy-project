package co.empathy.academy.search.configuration;




import co.empathy.academy.search.repository.ElasticConnection;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.jobrunr.configuration.JobRunr;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.mongo.MongoDBStorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class Config implements AsyncConfigurer {

    //private static final String hostname = "elasticsearch";
    private static final String hostname = "localhost";
    private static final int port = 9200;

    @Autowired
    private MongoClient mongoClient;


    @Bean
    public ElasticConnection elasticConnection() {
        return new ElasticConnection(RestClient.builder(new HttpHost(hostname, port)).build());
    }


    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        //InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        MongoDBStorageProvider storageProvider = new MongoDBStorageProvider(mongoClient);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

}
