package com.example.labmvc.controller;

import com.example.labmvc.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createUser_WithValidData_ReturnsCreatedUser() throws Exception {
        User user = new User();
        user.setName("Alice");
        user.setAge(20);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createUser_WithEmptyBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithBlankName_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setName("");
        user.setAge(30);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithNameTooLong_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setName("ThisNameIsWayTooLongAndExceedsTwentyCharacters");
        user.setAge(25);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithNullAge_ReturnsBadRequest() throws Exception {
        String json = "{\"name\":\"Bob\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithNegativeAge_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setName("Charlie");
        user.setAge(-1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithAgeTooHigh_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setName("David");
        user.setAge(150);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithBlankName_ReturnsBadRequest() throws Exception {
        User user = new User();
        user.setName("");
        user.setAge(30);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchUsers_WithParameters_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("name", "Ali")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());
    }
}
