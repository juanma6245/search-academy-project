package co.empathy.academy.search.service;

import co.empathy.academy.search.repository.ElasticConnection;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SearchServiceImplTest {

    @Mock
    private ElasticConnection connection;

    @InjectMocks
    private SearchServiceImpl searchService;


    @Test
    void search() throws ParseException, IOException {
        when(connection.getClusterName()).thenReturn("test");
        assertEquals(searchService.search("test"), "test");
        assertEquals(searchService.search(""), "Error: Query is blank");
    }

    @Test
    void getClusterName() throws ParseException, IOException {
        when(connection.getClusterName()).thenReturn("docker-cluster");
        assertEquals(searchService.getClusterName(), "docker-cluster");
    }
}