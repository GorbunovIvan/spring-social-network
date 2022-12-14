package org.example.controllers;

import org.example.dao.MessageDAO;
import org.example.dao.UserDAO;
import org.example.models.Message;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MessageDAO messageDAO;

    @GetMapping("/{companionId}")
    public String chat(@PathVariable int companionId, Model model,
                       @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        User user = userDAO.getCurrentUser(currentUserId);
        User companion = userDAO.read(companionId);

        List<Message> messages = messageDAO.getMessagesOfChat(user, companion);

        model.addAttribute("user", user);
        model.addAttribute("companion", companion);
        model.addAttribute("messages", messages);
        model.addAttribute("message", new Message(user, companion, ""));

        return "/messages/chat";
    }

    @PostMapping("/createNew/{id}")
    public String createNew(@PathVariable("id") int receiverId, @ModelAttribute Message message,
                            @CookieValue(value = "user-id", defaultValue = "") String currentUserId) {

        if (!userDAO.isAuthorized(currentUserId))
            return "redirect:/auth/login";

        User sender = userDAO.getCurrentUser(currentUserId);
        User receiver = userDAO.read(receiverId);

        message.setSender(sender);
        message.setReceiver(receiver);

        messageDAO.create(message);

        return "redirect:/chat/" + receiver.getId();
    }

}
