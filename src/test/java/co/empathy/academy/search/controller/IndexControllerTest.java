package co.empathy.academy.search.controller;

import co.empathy.academy.search.configuration.TestConfig;
import co.empathy.academy.search.repository.ElasticConnection;
import co.empathy.academy.search.service.IndexService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
class IndexControllerTest {

    @MockBean
    private IndexService indexService;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void index() {
        //TODO
    }

    @Test
    void deleteIndex() throws Exception {
        given(indexService.deleteIndex("test")).willReturn(true);
        assertEquals(true, indexService.deleteIndex("test"));
        mockMvc.perform(delete("/index/test"))
                .andExpect(status().isOk());
    }
}