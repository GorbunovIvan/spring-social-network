package org.example.dao;

import org.example.config.SpringConfig;
import org.example.models.FriendsRelations;
import org.example.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class FriendsRelationsDAOTest {

    private AnnotationConfigApplicationContext context;
    private DAO<FriendsRelations> friendsRelationsDAO;
    private final User inviter = new User("friendInviter", LocalDate.now());
    private final User receiver = new User("friendReceiver", LocalDate.now());

    private static final Logger logger = LoggerFactory.getLogger(FriendsRelationsDAOTest.class);

    @BeforeEach
    void setUp() {
        var context = new AnnotationConfigApplicationContext(SpringConfig.class);
        friendsRelationsDAO = context.getBean(FriendsRelationsDAO.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null)
            context.close();
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