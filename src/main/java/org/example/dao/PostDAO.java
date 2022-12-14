package org.example.dao;

import org.example.models.Post;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostDAO implements DAO<Post> {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;

    @Override
    public int create(Post post) {

//        if (post.getUser() == null)
//            post.setUser(userDAO.getCurrentUser());

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            post.setUser(session.get(User.class, post.getUser().getId()));
            session.persist(post);
            session.getTransaction().commit();
        }

        return post.getId();
    }

    @Override
    public Post read(int id) {

        Post post;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            post = session.get(Post.class, id);
            session.getTransaction().commit();
        }

        return post;
    }

    @Override
    public List<Post> readALl() {

        List<Post> posts;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            posts = session.createQuery("select post from Post post", Post.class).list();
            session.getTransaction().commit();
        }

        return posts;
    }

    @Override
    public void update(int id, Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User user = session.get(User.class, post.getUser().getId());

            Post postPersisted = session.get(Post.class, id);
            postPersisted.setUser(user);
            postPersisted.setText(post.getText());
            postPersisted.setTime(post.getTime());

            session.persist(postPersisted);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Post post = session.get(Post.class, id);
            if (post != null)
                session.delete(post);
            session.getTransaction().commit();
        }
    }
}
