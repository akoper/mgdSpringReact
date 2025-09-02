package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.UserAccount;
import com.example.mgdSpringReact.repository.UserAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserAccountRepository users;

    public UsersController(UserAccountRepository users) {
        this.users = users;
    }

    public static class UserSummary {
        public Long id;
        public String username;
        public UserSummary(Long id, String username) { this.id = id; this.username = username; }
    }

    @GetMapping
    public List<UserSummary> list() {
        return users.findAll().stream()
                .map(u -> new UserSummary(u.getId(), u.getUsername()))
                .toList();
    }
}
