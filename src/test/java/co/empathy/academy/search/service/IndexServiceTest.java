package co.empathy.academy.search.service;

import co.empathy.academy.search.common.DocumentStorage;
import co.empathy.academy.search.repository.ElasticConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class IndexServiceTest {
    @Mock
    private ElasticConnection elasticConnection;
    @Mock
    private TSVService TSVService;
    @Mock
    private DocumentStorage documentStorage;
    @InjectMocks
    private IndexService indexService = new IndexServiceImpl();


    @Test
    public void givenNewIndexName_whenCreateIndex_thenIndexCreated() throws IOException {
        String indexName = "test";
        given(elasticConnection.createIndex(indexName)).willReturn(true);
        given(elasticConnection.indexExists(indexName)).willReturn(true);
        indexService.createIndex(indexName);
        assertTrue(elasticConnection.indexExists(indexName));
    }

    @Test
    public void givenExitingIndex_whenDeleteIndex_thenIndexDeleted() throws IOException {
        String indexName = "test";
        given(elasticConnection.deleteIndex(indexName)).willReturn(true);
        assertTrue(indexService.deleteIndex(indexName));
        given(elasticConnection.indexExists(indexName)).willReturn(false);
        assertFalse(elasticConnection.indexExists(indexName));
    }

    @Test
    public void index() {
        //TODO
    }
}