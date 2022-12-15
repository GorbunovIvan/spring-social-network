package org.example.dao;

import org.example.config.SpringConfig;
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
class UserDAOTest {

    private UserDAO userDAO;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        userDAO = mockMvc.getDispatcherServlet()
                .getWebApplicationContext()
                .getBean(UserDAO.class);
    }

    @Test
    void testCreate() {

        User user = new User("testCreate", LocalDate.of(2005, 12, 5));
        userDAO.create(user);

        assertNotNull(user.getId());
    }

    @Test
    void testRead() {

        Integer id = userDAO.create(new User("testReadAll", LocalDate.of(1, 2, 3)));

        User user = userDAO.read(id);
        assertEquals(user.getId(), id);
    }

    @Test
    void testReadALl() {

        Integer id = userDAO.create(new User("testReadAll", LocalDate.of(1, 2, 3)));

        List<User> users = userDAO.readALl();
        assertTrue(users.stream().anyMatch(user -> user.getId().equals(id)));
    }

    @Test
    void testUpdate() {

        User user = new User("testUpdate", LocalDate.of(1, 2, 3));
        int id = userDAO.create(user);

        user.setName("updated");
        userDAO.update(id, user);

        assertEquals(userDAO.read(id).getName(), user.getName());
    }

    @Test
    void testDelete() {

        User user = new User("testDelete", LocalDate.of(1, 2, 3));
        int id = userDAO.create(user);

        userDAO.delete(id);

        assertNull(userDAO.read(id));
    }

    @Test
    void testSendMessage() {

        User sender = new User("testSendingMessage", LocalDate.of(1, 2, 3));
        User receiver = new User("testReceiverMessage", LocalDate.of(1, 2, 3));

        sender.sendMessage(receiver, "test sending message");

        int senderId = userDAO.create(sender);

        assertTrue(userDAO.read(senderId).getMessagesSent().stream()
                .anyMatch(m -> m.getText().equals("test sending message")));
    }

    @Test
    void testAddPost() {

        User user = new User("testAddingPost", LocalDate.of(1, 2, 3));

        user.addPost("test adding post");

        int userId = userDAO.create(user);

        assertTrue(userDAO.read(userId).getPosts().stream()
                .anyMatch(p -> p.getText().equals("test adding post")));
    }

    @Test
    void testAddFriend() {

        User user = new User("test friend inviter", LocalDate.of(1, 2, 3));
        User friend = new User("test friend receiver", LocalDate.of(1, 2, 3));

        Integer friendId = userDAO.create(friend);

        user.addFriend(friend);

        int userId = userDAO.create(user);

        assertTrue(userDAO.read(userId).getFriendsInvited().stream()
                .anyMatch(f -> f.getReceiver().getId().equals(friendId)));
    }

    @Test
    void testIsLoginFree() {
        assertTrue(userDAO.isLoginFree("freeLogin"));
        assertFalse(userDAO.isLoginFree("test"));
    }
}