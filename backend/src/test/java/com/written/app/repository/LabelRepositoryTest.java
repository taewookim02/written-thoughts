package com.written.app.repository;

import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LabelRepositoryTest {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void LabelRepository_Save_ReturnLabel() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Label label = Label.builder()
                .name("test label")
                .user(user)
                .build();

        // when
        Label savedLabel = labelRepository.save(label);

        // then
        assertThat(savedLabel).isNotNull();
        assertThat(savedLabel.getName()).isEqualTo("test label");
        assertThat(savedLabel.getUser()).isEqualTo(user);
    }


    @Test
    public void LabelRepository_FindById_ReturnLabel() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Label label = Label.builder()
                .name("test label")
                .user(user)
                .build();
        label = labelRepository.save(label);

        Label label2 = Label.builder()
                .name("test label2")
                .user(user)
                .build();
        label2 = labelRepository.save(label2);


        // when
        Label resultLabel = labelRepository.findById(label2.getId()).get();

        // then
        assertThat(resultLabel).isNotNull();
        assertThat(resultLabel.getName()).isEqualTo("test label2");
        assertThat(resultLabel.getUser()).isEqualTo(user);
    }


}
