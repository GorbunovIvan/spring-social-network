package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.Message;
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
class MessageDAOTest {

    private AnnotationConfigApplicationContext context;
    private DAO<Message> messageDAO;
    private final User sender = new User("userForMessageSending", LocalDate.now());
    private final User receiver = new User("userForMessageReceiving", LocalDate.now());

    @BeforeEach
    void setUp() {
        var context = new AnnotationConfigApplicationContext(SpringConfig.class);
        messageDAO = context.getBean(MessageDAO.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null)
            context.close();
    }

    @Test
    void testCreate() {

        Message message = new Message(sender, receiver, "testCreate");
        messageDAO.create(message);

        assertNotNull(message.getId());
    }

    @Test
    void testRead() {

        Integer id = messageDAO.create(new Message(sender, receiver, "testRead"));

        Message message = messageDAO.read(id);
        assertEquals(message.getId(), id);
    }

    @Test
    void testReadALl() {

        Integer id = messageDAO.create(new Message(sender, receiver, "testReadAll"));

        List<Message> messages = messageDAO.readALl();
        assertTrue(messages.stream().anyMatch(Message -> Message.getId().equals(id)));
    }

    @Test
    void testUpdate() {

        Message message = new Message(sender, receiver, "testUpdate");
        int id = messageDAO.create(message);

        message.setText("testUpdate updated");
        messageDAO.update(id, message);

        assertEquals(messageDAO.read(id).getText(), message.getText());
    }

    @Test
    void testDelete() {

        int id = messageDAO.create(new Message(sender, receiver, "testDelete"));

        messageDAO.delete(id);

        assertNull(messageDAO.read(id));
    }
}