package co.empathy.academy.search.service;

import co.empathy.academy.search.service.client.SearchEngine;
import co.empathy.academy.search.service.client.SearchEngineImpl;
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
class SearchServiceImplTest {

    @Mock
    private SearchEngineImpl searchEngine;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    void search() throws ParseException, IOException {
        when(searchEngine.executeQuery("test")).thenReturn("test");
        assertEquals(searchService.search("test"), searchEngine.executeQuery("test"));
        assertEquals(searchService.search(""), "Error: Query is blank");
    }

    @Test
    void getClusterName() throws ParseException, IOException {
        when(searchEngine.executeQuery("queryCluster")).thenReturn("docker-cluster");
        assertEquals(searchService.getClusterName(), "docker-cluster");
    }
}