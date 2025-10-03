package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/user/profile")
    public String userProfilePage() {
        return "user/profile";
    }

    @GetMapping("/admin/users")
    public String adminUsersPage() {
        return "admin/users";
    }

    @GetMapping("/admin/users/new")
    public String newUserPage() {
        return "admin/new-user";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "admin/edit-user";
    }
}
