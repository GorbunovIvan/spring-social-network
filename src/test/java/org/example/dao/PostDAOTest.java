package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.Post;
import org.example.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
class PostDAOTest {

    private DAO<Post> postDAO;
    private final User user = new User("userForPosting", LocalDate.now());

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        postDAO = mockMvc.getDispatcherServlet()
                .getWebApplicationContext()
                .getBean(PostDAO.class);
    }

    @Test
    void testCreate() {

        Post Post = new Post(user, "testCreate");
        postDAO.create(Post);

        assertNotNull(Post.getId());
    }

    @Test
    void testRead() {

        Integer id = postDAO.create(new Post(user, "testRead"));

        Post Post = postDAO.read(id);
        assertEquals(Post.getId(), id);
    }

    @Test
    void testReadALl() {

        Integer id = postDAO.create(new Post(user, "testReadAll"));

        List<Post> Posts = postDAO.readALl();
        assertTrue(Posts.stream().anyMatch(Post -> Post.getId().equals(id)));
    }

    @Test
    void testUpdate() {

        Post Post = new Post(user, "testUpdate");
        int id = postDAO.create(Post);

        Post.setText("testUpdate updated");
        postDAO.update(id, Post);

        assertEquals(postDAO.read(id).getText(), Post.getText());
    }

    @Test
    void testDelete() {

        int id = postDAO.create(new Post(user, "testDelete"));

        postDAO.delete(id);

        assertNull(postDAO.read(id));
    }
}