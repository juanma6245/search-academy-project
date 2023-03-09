package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.ExistingUserException;
import co.empathy.academy.search.exception.UserNotFoundException;
import co.empathy.academy.search.model.User;
import co.empathy.academy.search.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import javax.print.attribute.standard.Media;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private User test;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @BeforeEach
    void setUp() throws ExistingUserException {
        test = new User(1L,"test","test");
        this.userService.save(test);
    }

    @AfterEach
    void tearDown() throws UserNotFoundException {
        this.userService.delete(1L);
    }

    @Test
    public void whenGetUsers_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void givenUser_whenGetUserById_thenReturnUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", test.getId().intValue())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(test.getId().intValue())))
                .andExpect(jsonPath("$.name", is(test.getName())))
                .andExpect(jsonPath("$.email", is(test.getEmail())));
    }

    @Test
    public void givenFailUser_WhenGetUser_thenStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 23)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void givenNewUser_WhenCreateUser_thenStatus201() throws Exception {
        User newUser = new User(100L,"new","new");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().is(201));
    }
    @Test
    public void givenExistingUser_WhenCreateUser_thenStatus400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(test)))
                .andExpect(status().is(400));
    }

    @Test
    public void givenNewName_WhenModifyUser_thenStatus204_thenUserIsModified() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"newNameTest\"}"))
                .andExpect(status().is(204));
        assertEquals(this.userService.get(1L).getName(), "newNameTest");
    }

    @Test
    public void givenUserId_WhenDelete_ThenStatus204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
        this.setUp();
    }
}