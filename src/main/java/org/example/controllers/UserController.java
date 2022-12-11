package org.example.controllers;

import org.example.dao.UserDAO;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("users", userDAO.readALl());
        return "users/all";
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable int id, Model model) {

        User user = userDAO.read(id);

        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());

        return "users/profile";
    }

    @GetMapping("/{id}/friends")
    public String friends(@PathVariable int id, Model model) {

        User user = userDAO.read(id);

        model.addAttribute("user", user);
        model.addAttribute("friends", user.getFriends());

        return "users/friends";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("user", userDAO.read(id));
        return "/users/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User user) {
        userDAO.update(user.getId(), user);
        return "redirect:/users/" + user.getId();
    }

}
