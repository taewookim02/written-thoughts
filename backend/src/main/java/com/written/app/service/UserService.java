package com.written.app.service;

import com.written.app.dto.ChangePasswordRequestDto;
import com.written.app.model.User;
import com.written.app.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public void changePassword(ChangePasswordRequestDto request, Principal connectedUser) {
        // get current user
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getPassword() = " + user.getPassword());
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        // check if confirmation password matches
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        // update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save new password
        userRepository.save(user);
    }
}
