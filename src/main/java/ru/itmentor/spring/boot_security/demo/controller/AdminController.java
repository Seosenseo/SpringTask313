package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String helloAdmin(Principal principal, Model model) {
//        model.addAttribute("username", userService.findByUsername(principal.getName()));
        model.addAttribute("listusers", userService.findAllUsers());
        return "admin";
    }

    @GetMapping("/adduser")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "adduser";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/clear-table")
    public String showCleanUsersTable() {
        return "clear-table";
    }

    @PostMapping("/clear-table")
    public String cleanUsersTable() {
        userService.clearUsers();
        return "/admin";
    }
}
