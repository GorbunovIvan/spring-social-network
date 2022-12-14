package org.example.dao;

import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO implements DAO<User> {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
//        currentUser = read(52);
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
            user = session.get(User.class, id);
        }

        return user;
    }

    public User read(String login, String password) {

        User user;

        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("select u from User u where login=?1 and password=?2", User.class);
            query.setParameter(1, login);
            query.setParameter(2, password);

            List<User> users = query.getResultList();

            if (users.isEmpty())
                return null;

            user = users.get(0);
        }

        return user;
    }

    @Override
    public List<User> readALl() {

        List<User> users;

        try (Session session = sessionFactory.openSession()) {
            users = session.createQuery("select user from User user", User.class).list();
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

    public boolean isLoginFree(String login) {
        try(Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createNativeQuery("select 1 from users u where u.login=?1", Integer.class);
            query.setParameter(1, login);
            return query.getResultList().isEmpty();
        }
    }

    public void stopIfNotAuthorized(String userIdFromCookies) {
        if (!isAuthorized(userIdFromCookies))
            throw new IllegalStateException("you are not authorized");
    }

    public boolean isAuthorized(String userIdFromCookies) {
        return getCurrentUser(userIdFromCookies) != null;
    }

    public User getCurrentUser(String userIdFromCookies) {

        if (userIdFromCookies.isEmpty())
            return null;

        User user = read(Integer.parseInt(userIdFromCookies));

        return user;
    }

}
