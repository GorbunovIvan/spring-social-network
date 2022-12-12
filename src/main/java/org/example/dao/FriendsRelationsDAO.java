package org.example.dao;

import jakarta.persistence.Query;
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

        FriendsRelations friendsRelationsPersisted;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User user = session.get(User.class, friendsRelations.getInviter().getId());
            User friend = session.get(User.class, friendsRelations.getReceiver().getId());
            friendsRelationsPersisted = new FriendsRelations(user, friend);

            session.persist(friendsRelationsPersisted);

            session.getTransaction().commit();
        }

        return friendsRelationsPersisted.getId();
    }

    @Override
    public FriendsRelations read(int id) {

        FriendsRelations friendsRelations;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            friendsRelations = session.get(FriendsRelations.class, id);
            session.getTransaction().commit();
        }

        return friendsRelations;
    }

    public FriendsRelations read(int friend1Id, int friend2Id) {

        FriendsRelations friendsRelations;

        try (Session session = sessionFactory.openSession()) {

            session.getTransaction().begin();

            Query query = session.createNativeQuery("select * from friends_relations f WHERE (f.inviter_id = ?1 AND f.receiver_id = ?2) OR (f.inviter_id = ?2 AND f.receiver_id = ?1)", FriendsRelations.class);
            query.setParameter(1, friend1Id);
            query.setParameter(2, friend2Id);

            friendsRelations = (FriendsRelations)query.getSingleResult();

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
