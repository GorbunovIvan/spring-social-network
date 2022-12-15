package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.Message;
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
class MessageDAOTest {

    private DAO<Message> messageDAO;
    private final User sender = new User("userForMessageSending", LocalDate.now());
    private final User receiver = new User("userForMessageReceiving", LocalDate.now());

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        messageDAO = mockMvc.getDispatcherServlet()
                .getWebApplicationContext()
                .getBean(MessageDAO.class);
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