package org.example.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class User implements DAO<User> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void create(User user) {
        try(Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public User read(int id) {

        User user = null;

        try(Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        }

        return user;
    }

    @Override
    public List<User> readALl() {

        List<User> users;

        try(Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            users = session.createQuery("select user from User user", User.class).list();
            session.getTransaction().commit();
        }

        return users;
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.delete(user);
            session.getTransaction().commit();
        }
    }
}
