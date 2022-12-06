package org.example;

import org.example.config.SpringConfig;
import org.example.dao.DAO;
import org.example.dao.UserDAO;
import org.example.models.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Application {

    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(SpringConfig.class);

        DAO<User> userDAO = context.getBean(UserDAO.class);
        userDAO.create(new User("Ivan", LocalDate.of(2005, 12, 5)));

        context.close();

    }

}
