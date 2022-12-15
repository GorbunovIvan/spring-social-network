package org.example.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import org.example.config.SpringConfig;
import org.example.models.User;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    void testShowAll() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("users/all"));
    }

    @Test
    void testProfile() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + testUserId()))
                .andExpect(MockMvcResultMatchers.view().name("users/profile"));
    }

    @Test
    void testFriends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + testUserId() + "/friends"))
                .andExpect(MockMvcResultMatchers.view().name("users/friends"));
    }

    @Test
    void testEdit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/edit"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void testEditAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/edit").cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("users/edit"));
    }

    @Test
    void testUpdate() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void testUpdateAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update").cookie(getCookies()))
                .andDo(print())
                .andExpect(jsonPath("$.user").value(new User(1, "test")))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users/1"));
    }

    @Test
    void testAddToFriends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/addToFriends/2"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void testAddToFriendsAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/addToFriends/2").cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users/2"));
    }

    @Test
    void testDeleteFromFriends() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/deleteFromFriends/2"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void testDeleteFromFriendsAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/deleteFromFriends/2").cookie(getCookies()))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users/2"));
    }

    private Cookie getCookies() {
        return new Cookie("user-id", testUserId());
    }

    private String testUserId() {
        return "142";
    }
}