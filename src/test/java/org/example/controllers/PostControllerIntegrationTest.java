package org.example.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
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
    void createNewAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/new").cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("posts/post"));
    }

    @Test
    void edit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/" + testPostId()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void editAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/" + testPostId()).cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("posts/post"));
    }

    @Test
    void createUpdate() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/posts"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void createUpdateAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/posts").cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users/" + testUserId()));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/delete/" + testPostId()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void deleteAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/delete/" + testPostId()).cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users/" + testUserId()));
    }

    private Cookie getCookies() {
        return new Cookie("user-id", testUserId());
    }

    private String testUserId() {
        return "142";
    }

    private String testPostId() {
        return "19";
    }
}