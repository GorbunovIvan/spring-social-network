package org.example;

import org.example.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(SpringConfig.class);
        context.close();

    }
}
