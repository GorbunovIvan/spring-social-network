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
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesAuthController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("authController"));
    }

    @Test
    void loginForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
                .andExpect(MockMvcResultMatchers.view().name("auth/login"));
    }

    @Test
    void login() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"));
    }

    @Test
    void registerForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/auth/register"))
                .andExpect(MockMvcResultMatchers.view().name("auth/register"));
    }

    @Test
    void register() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/register"))
                .andReturn();
//                .andExpect(MockMvcResultMatchers.view().name("redirect:/users"));
    }

    @Test
    void logout() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/auth/logout"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/users"));
    }
}