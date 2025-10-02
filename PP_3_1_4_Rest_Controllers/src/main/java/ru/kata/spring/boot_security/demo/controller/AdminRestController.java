package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error");
        }

        // Проверка на существующий email
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        // Обрабатываем роли - убеждаемся, что они существуют в базе
        if (user.getRoles() != null) {
            Set<Role> managedRoles = user.getRoles().stream()
                    .map(role -> {
                        Role existingRole = roleService.findByName(role.getName());
                        return existingRole != null ? existingRole : role;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(managedRoles);
        }

        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error");
        }

        // Проверка что email не занят другим пользователем
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        // Обрабатываем роли
        if (user.getRoles() != null) {
            Set<Role> managedRoles = user.getRoles().stream()
                    .map(role -> {
                        Role existingRole = roleService.findByName(role.getName());
                        return existingRole != null ? existingRole : role;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(managedRoles);
        }

        userService.update(id, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}