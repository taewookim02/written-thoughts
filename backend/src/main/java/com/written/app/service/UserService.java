package com.written.app.service;

import com.written.app.model.User;
import com.written.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
