package org.example;

import org.example.config.SpringConfig;
import org.example.dao.DAO;
import org.example.dao.UserDAO;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.time.LocalDate;

public class Application {

    public static void main(String[] args) {

//        SpringConfig springConfig = new SpringConfig();
//
//        try (Session session = springConfig.sessionFactory().openSession()) {
//            session.getTransaction().begin();
//            session.persist(new User("Ivan", LocalDate.of(2005, 12, 5)));
//            session.getTransaction().commit();
//        }

        var context = new AnnotationConfigApplicationContext(SpringConfig.class);

        DAO<User> userDAO = context.getBean(UserDAO.class);
        userDAO.create(new User("Ivan", LocalDate.of(2005, 12, 5)));

        context.close();

    }

}
