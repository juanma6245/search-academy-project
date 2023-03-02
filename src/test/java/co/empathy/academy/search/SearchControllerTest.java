package co.empathy.academy.search;

import co.empathy.academy.search.controller.SearchController;
import co.empathy.academy.search.model.SearchResponse;
import co.empathy.academy.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mvc;

    @Mock
    private SearchService searchServiceMock;


    @InjectMocks
    private SearchController searchController;
    @Test
    void givenquery_returnquery_cluster_name() throws Exception{

        when(searchServiceMock.getClusterName()).thenReturn("docker-cluster");
        String clusterName = searchServiceMock.getClusterName();
        SearchResponse response = new SearchResponse();
        response.setQuery("test");
        response.setClusterName(clusterName);
        assertEquals(searchController.search("test").getBody(), response);

    }
}
