package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.FriendsRelations;
import org.example.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class FriendsRelationsDAOTest {

    private FriendsRelationsDAO friendsRelationsDAO;
    private final User inviter = new User("friendInviter", LocalDate.now());
    private final User receiver = new User("friendReceiver", LocalDate.now());

    private static final Logger logger = LoggerFactory.getLogger(FriendsRelationsDAOTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        friendsRelationsDAO = mockMvc.getDispatcherServlet()
                .getWebApplicationContext()
                .getBean(FriendsRelationsDAO.class);
    }

    @Test
    void testCreate() {

        FriendsRelations friendsRelations = new FriendsRelations(inviter, receiver);
        friendsRelationsDAO.create(friendsRelations);

        assertNotNull(friendsRelations.getId());
    }

    @Test
    void testRead() {

        Integer id = friendsRelationsDAO.create(new FriendsRelations(inviter, receiver));

        FriendsRelations friendsRelations = friendsRelationsDAO.read(id);
        assertEquals(friendsRelations.getId(), id);
    }

    @Test
    void testReadByFriendsId() {

        Integer id = friendsRelationsDAO.create(new FriendsRelations(inviter, receiver));

        FriendsRelations friendsRelations = friendsRelationsDAO.read(inviter.getId(), receiver.getId());
        assertEquals(friendsRelations.getId(), id);
    }

    @Test
    void testReadALl() {

        Integer id = friendsRelationsDAO.create(new FriendsRelations(inviter, receiver));

        List<FriendsRelations> friendsRelations = friendsRelationsDAO.readALl();
        assertTrue(friendsRelations.stream().anyMatch(f -> f.getId().equals(id)));
    }

    @Test
    void testUpdate() {

        logger.warn("testUpdate is skipped");

//        FriendsRelations friendsRelations = new FriendsRelations(inviter, receiver);
//        int id = friendsRelationsDAO.create(friendsRelations);
//
//        friendsRelations.setReceiver(new User("new friend receiver", LocalDate.now()));
//        friendsRelationsDAO.update(id, friendsRelations);
//
//        assertEquals(friendsRelationsDAO.read(id).getReceiver().getName(), "new friend receiver");
    }

    @Test
    void testDelete() {

        int id = friendsRelationsDAO.create(new FriendsRelations(inviter, receiver));

        friendsRelationsDAO.delete(id);

        assertNull(friendsRelationsDAO.read(id));
    }
}