package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    void save(User user);
    User findById(Long id);
    List<User> findAll();
    void delete(Long id);
    void update(Long id, User user);
    User findByEmail(String email);
}


