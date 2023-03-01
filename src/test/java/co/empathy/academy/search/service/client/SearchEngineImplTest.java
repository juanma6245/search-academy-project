package co.empathy.academy.search.service.client;

import co.empathy.academy.ElasticConnection;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SearchEngineImplTest {

    @Mock
    private ElasticConnection elasticConnectionMock;
    @InjectMocks
    private SearchEngineImpl searchEngine;
    @Test
    void executeQuery() throws ParseException, IOException {
        when(elasticConnectionMock.getClusterName()).thenReturn("docker-cluster");
        assertEquals(searchEngine.executeQuery("test"), "docker-cluster");
    }
}