package org.example.controllers;

import jakarta.servlet.ServletContext;
import org.example.config.SpringConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesUserController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test
    void showAll() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("users/all"));
    }

    @Test
    void profile() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.view().name("users/profile"));
    }

    @Test
    void friends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends"))
                .andExpect(MockMvcResultMatchers.view().name("users/friends"));
    }

    @Test
    void edit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/edit"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void update() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void addToFriends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/addToFriends/1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void deleteFromFriends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/deleteFromFriends/1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }
}