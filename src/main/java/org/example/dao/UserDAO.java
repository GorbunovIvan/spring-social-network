package org.example.dao;

import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO implements DAO<User> {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public int create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
        }
        return user.getId();
    }

    @Override
    public User read(int id) {

        User user;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        }

        return user;
    }

    @Override
    public List<User> readALl() {

        List<User> users;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            users = session.createQuery("select user from User user", User.class).list();
            session.getTransaction().commit();
        }

        return users;
    }

    @Override
    public void update(int id, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User userPersisted = session.get(User.class, id);
            userPersisted.setName(user.getName());
            userPersisted.setBirthDay(user.getBirthDay());

            session.persist(userPersisted);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User user = session.get(User.class, id);
            if (user != null)
                session.delete(user);
            session.getTransaction().commit();
        }
    }

    // temporary
    public User getCurrentUser() {
        return read(52);
    }
}
