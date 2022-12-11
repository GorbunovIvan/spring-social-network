package org.example.controllers;

import org.example.dao.MessageDAO;
import org.example.dao.UserDAO;
import org.example.models.Message;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MessageDAO messageDAO;

    @GetMapping("/{userId}/{companionId}")
    public String chat(@PathVariable int userId, @PathVariable int companionId, Model model) {

        User user = userDAO.read(userId);
        User companion = userDAO.read(companionId);

        List<Message> messages = messageDAO.getMessagesOfChat(user, companion);

        model.addAttribute("user", user);
        model.addAttribute("companion", companion);
        model.addAttribute("messages", messages);

        return "/messages/chat";
    }

}
