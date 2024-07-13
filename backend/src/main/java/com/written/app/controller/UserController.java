package com.written.app.controller;

import com.written.app.model.Entry;
import com.written.app.model.User;
import com.written.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // user list
    @GetMapping("/list")
    public List<User> findAllUser() {
        return userService.findAllUser();
    }

    // user by id
    @GetMapping("/{id}")
    public User findUserById(
            @PathVariable Integer id
    ) {
        return userService.findUserById(id);
    }


}
