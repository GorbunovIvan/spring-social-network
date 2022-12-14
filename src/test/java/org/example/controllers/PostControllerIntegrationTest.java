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
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesPostController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("postController"));
    }

    @Test
    void createNew() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/new"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void edit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void createUpdate() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/posts"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/delete/1"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }
}