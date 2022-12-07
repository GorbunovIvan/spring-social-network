package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.Post;
import org.example.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class PostDAOTest {

    private AnnotationConfigApplicationContext context;
    private DAO<Post> postDAO;
    private final User user = new User("userForPosting", LocalDate.now());

    @BeforeEach
    void setUp() {
        var context = new AnnotationConfigApplicationContext(SpringConfig.class);
        postDAO = context.getBean(PostDAO.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null)
            context.close();
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