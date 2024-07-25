package com.written.app.repository;

import com.written.app.model.Role;
import com.written.app.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

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

    @Test
    public void UserRepository_SoftDelete_MarkUserIsDeleted() {
        // given
        User userToDelete = User.builder()
                .email("delete@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        User savedUserToSoftDelete = userRepository.save(userToDelete);
        entityManager.flush();
        entityManager.clear();

        // when
        userRepository.softDeleteById(savedUserToSoftDelete.getId());


        // then
        User updatedUser = userRepository.findById(savedUserToSoftDelete.getId()).orElseThrow();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.isDeleted()).isTrue();
        assertThat(updatedUser.getEmail()).isEqualTo("delete@example.com");
    }

    @Test
    public void UserRepository_FindByIdAndIsDeletedFalse_ReturnUser() {
        // given
        User userToDelete = User.builder()
                .email("delete@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        User savedUserToDelete = userRepository.save(userToDelete);

        User userToKeep = User.builder()
                .email("keep@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        User savedUserToKeep = userRepository.save(userToKeep);

        // flush to db insert
        entityManager.flush();
        entityManager.clear();

        // soft delete
        userRepository.softDeleteById(savedUserToDelete.getId());


        // when
        Optional<User> resultDeletedUser = userRepository.findByIdAndIsDeletedFalse(savedUserToDelete.getId());
        Optional<User> resultKeptUser = userRepository.findByIdAndIsDeletedFalse(savedUserToKeep.getId());

        // then
        assertThat(resultDeletedUser).isEmpty();
        assertThat(resultKeptUser).isPresent();
        assertThat(resultKeptUser.get().getEmail()).isEqualTo("keep@example.com");
        assertThat(resultKeptUser.get().isDeleted()).isFalse();

        Optional<User> softDeletedUser = userRepository.findById(savedUserToDelete.getId());
        assertThat(softDeletedUser).isPresent();
        assertThat(softDeletedUser.get().isDeleted()).isTrue();
    }

}
