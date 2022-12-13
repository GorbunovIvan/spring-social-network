package org.example.controllers;

import org.example.dao.UserDAO;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User userForAuthentication) {

        User user = userDAO.read(userForAuthentication.getLogin(), userForAuthentication.getPassword());

        if (user == null)
            return "redirect:/auth/login";

        userDAO.setCurrentUser(user);

        return "redirect:/users";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {

        if ((user.getLogin() == null || user.getLogin().isBlank())
            || (user.getName() == null || user.getName().isBlank())
            || (user.getPassword() == null || user.getPassword().isBlank()))
            throw new IllegalArgumentException("invalid or empty parameters");

        if (!userDAO.isLoginFree(user.getLogin()))
            throw new IllegalArgumentException("login is reserved");

        userDAO.create(user);
        userDAO.setCurrentUser(user);

        return "redirect:/users/edit";
    }

}
