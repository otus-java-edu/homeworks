package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.repository.UserRepository;
import ru.otus.crm.model.User;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/user/list"})
    public String usersListView(Model model) {
        var users = userRepository.findAll();
        model.addAttribute("users", users);
        return "usersList";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User(-1, "","", "", false));
        return "userCreate";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute() User user) {
        userRepository.save(user);
        return new RedirectView("/", true);
    }
}
