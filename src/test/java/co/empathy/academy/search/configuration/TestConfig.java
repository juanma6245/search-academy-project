package co.empathy.academy.search.configuration;

import com.mongodb.client.MongoClient;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.mongo.MongoDBStorageProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @MockBean
    private MongoClient mongoClient;
    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        //MongoDBStorageProvider storageProvider = new MongoDBStorageProvider(mongoClient);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }
}
