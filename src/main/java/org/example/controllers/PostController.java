package org.example.controllers;

import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.models.Post;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostDAO postDAO;
    @Autowired
    private UserDAO userDAO;

    @GetMapping("/new")
    public String createNew(Model model) {
        model.addAttribute("post", new Post());
        return "posts/post";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("post", postDAO.read(id));
        return "posts/post";
    }

    @PostMapping
    public String createUpdate(@ModelAttribute Post post) {

        post.setUser(userDAO.getCurrentUser());

        if (post.getId() == null)
            postDAO.create(post);
        else
            postDAO.update(post.getId(), post);

        return "redirect:/users/" + post.getUser().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {

        Post post = postDAO.read(id);

        postDAO.delete(id);

        return "redirect:/users/" + post.getUser().getId();
    }

}
