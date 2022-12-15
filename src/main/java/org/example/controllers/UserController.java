package org.example.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.FriendsRelationsDAO;
import org.example.dao.UserDAO;
import org.example.models.FriendsRelations;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private FriendsRelationsDAO friendsRelationsDAO;

    @GetMapping
    public String showAll(Model model,
                          @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        User currentUser = userDAO.getCurrentUser(currentUserId);

        model.addAttribute("users", userDAO.readALl());
        model.addAttribute("user", currentUser);
        model.addAttribute("isAuthorized", currentUser != null);

        return "users/all";
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable int id, Model model,
            @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        User user = userDAO.read(id);
        User currentUser = userDAO.getCurrentUser(currentUserId);

        model.addAttribute("user", user);
        model.addAttribute("isAuthorized", currentUser != null);
        model.addAttribute("isCurrentUser", user.equals(currentUser));
        model.addAttribute("isFriend", user.getFriends().contains(currentUser));
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

    @GetMapping("/edit")
    public String edit(Model model,
                       @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        model.addAttribute("user", userDAO.getCurrentUser(currentUserId));
        return "users/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User user,
                         @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        userDAO.update(user.getId(), user);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/addToFriends/{id}")
    public String addToFriends(@PathVariable int id,
                               @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        User user = userDAO.getCurrentUser(currentUserId);
        User friend = userDAO.read(id);

        if (friend.equals(user))
            throw new IllegalArgumentException("you cannot add yourself to friends");

        if (user.getFriends().contains(friend))
            throw new IllegalArgumentException("it's your friend already");

        user.addFriend(friend);

        FriendsRelations friendsRelations = user.addFriend(friend);

        friendsRelationsDAO.create(friendsRelations);

        return "redirect:/users/" + friend.getId();
    }

    @GetMapping("/deleteFromFriends/{id}")
    public String deleteFromFriends(@PathVariable int id,
                                    @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        User friend = userDAO.read(id);
        User user = userDAO.getCurrentUser(currentUserId);

        if (friend.equals(user))
            throw new IllegalArgumentException("you cannot delete yourself from friends");

        if (!friend.getFriends().contains(user))
            throw new IllegalArgumentException("it's not your friend");

        FriendsRelations friendsRelations = friendsRelationsDAO.read(user.getId(), friend.getId());
        friendsRelationsDAO.delete(friendsRelations.getId());

        return "redirect:/users/" + friend.getId();
    }

}
