package org.example.dao;

import org.example.models.Message;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDAO implements DAO<Message> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int create(Message message) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(message);
            session.getTransaction().commit();
        }
        return message.getId();
    }

    @Override
    public Message read(int id) {

        Message message;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            message = session.get(Message.class, id);
            session.getTransaction().commit();
        }

        return message;
    }

    @Override
    public List<Message> readALl() {

        List<Message> messages;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            messages = session.createQuery("select message from Message message", Message.class).list();
            session.getTransaction().commit();
        }

        return messages;
    }

    @Override
    public void update(int id, Message message) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User sender = session.get(User.class, message.getSender().getId());
            User receiver = session.get(User.class, message.getReceiver().getId());

            Message messagePersisted = session.get(Message.class, id);
            messagePersisted.setSender(sender);
            messagePersisted.setReceiver(receiver);
            messagePersisted.setText(message.getText());
            messagePersisted.setTime(message.getTime());

            session.persist(messagePersisted);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Message message = session.get(Message.class, id);
            if (message != null)
                session.delete(message);
            session.getTransaction().commit();
        }
    }
}
