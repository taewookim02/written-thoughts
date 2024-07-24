package com.written.app.repository;

import com.written.app.model.Role;
import com.written.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @Test
    public void UserRepository_Delete_RemoveUser() {
        // given
        User userToDelete = User.builder()
                .email("delete@example.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        User savedUserToDelete = userRepository.save(userToDelete);


        // when
        userRepository.delete(userToDelete);

        // then
        Optional<User> deletedUser = userRepository.findById(savedUserToDelete.getId());

        assertThat(deletedUser).isEmpty();
    }

}
