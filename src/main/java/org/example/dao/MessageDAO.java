package org.example.dao;

import jakarta.persistence.Query;
import org.example.models.Message;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class MessageDAO implements DAO<Message> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int create(Message message) {

        Message messagePersisted;

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User sender = message.getSender();
            User receiver = message.getReceiver();

            if (sender.getId() != null)
                sender = session.get(User.class, sender.getId());

            if (receiver.getId() != null)
                receiver = session.get(User.class, receiver.getId());

            messagePersisted = new Message(sender, receiver, message.getText());
            session.persist(messagePersisted);

            session.getTransaction().commit();
        }

        return messagePersisted.getId();
    }

    @Override
    public Message read(int id) {

        Message message;

        try (Session session = sessionFactory.openSession()) {
            message = session.get(Message.class, id);
        }

        return message;
    }

    @Override
    public List<Message> readALl() {

        List<Message> messages;

        try (Session session = sessionFactory.openSession()) {
            messages = session.createQuery("select message from Message message", Message.class).list();
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

    public List<Message> getMessagesOfChat(User user1, User user2) {

        List<Message> messages;

        try (Session session = sessionFactory.openSession()) {

            Query query = session.createQuery("from Message m where (m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1)");
            query.setParameter(1, user1);
            query.setParameter(2, user2);

            messages = query.getResultList();
        }

        Collections.sort(messages);

        return messages;
    }
}
