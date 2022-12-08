package org.example.controllers;

import org.example.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping()
    public String showAll(Model model) {
        System.out.println("show all");
        model.addAttribute("users", userDAO.readALl());
        return "users/all";
    }

}
