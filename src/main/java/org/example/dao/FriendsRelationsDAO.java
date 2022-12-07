package org.example.dao;

import org.example.models.FriendsRelations;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendsRelationsDAO implements DAO<FriendsRelations> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int create(FriendsRelations friendsRelations) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(friendsRelations);
            session.getTransaction().commit();
        }
        return friendsRelations.getId();
    }

    @Override
    public FriendsRelations read(int id) {

        FriendsRelations friendsRelations;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            friendsRelations =  session.get(FriendsRelations.class, id);
            session.getTransaction().commit();
        }

        return friendsRelations;
    }

    @Override
    public List<FriendsRelations> readALl() {

        List<FriendsRelations> friendsRelations;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            friendsRelations = session.createQuery("select friendsRelations from FriendsRelations friendsRelations", FriendsRelations.class).list();
            session.getTransaction().commit();
        }

        return friendsRelations;
    }

    @Override
    public void update(int id, FriendsRelations friendsRelations) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User receiver = session.get(User.class, friendsRelations.getReceiver().getId());

            FriendsRelations friendsRelationsPersisted = session.get(FriendsRelations.class, id);
            friendsRelationsPersisted.setReceiver(receiver);

            session.persist(friendsRelationsPersisted);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            FriendsRelations friendsRelations = session.get(FriendsRelations.class, id);
            if (friendsRelations != null)
                session.delete(friendsRelations);
            session.getTransaction().commit();
        }
    }
}
