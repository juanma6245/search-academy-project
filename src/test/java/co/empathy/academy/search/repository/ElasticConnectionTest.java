package co.empathy.academy.search.repository;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.empathy.academy.search.common.DocumentStorage;
import co.empathy.academy.search.model.title.Basic;
import co.empathy.academy.search.model.title.Crew;
import co.empathy.academy.search.model.title.Rating;
import co.empathy.academy.search.model.title.Title;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ElasticConnectionTest {
    @Mock
    private DocumentStorage documentStorage;
    @Mock
    private RestClient restClient;
    @InjectMocks
    private ElasticConnection elasticConnection = new ElasticConnection(restClient);

    @Test
    public void whenIndex_thenClientIndex() throws IOException {
        //TODO
    }
}