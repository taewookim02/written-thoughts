package com.written.app.repository;

import com.github.javafaker.Pokemon;
import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EntryRepositoryTest {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Test
    public void Given_Entry_When_Save_Then_ReturnEntry() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Label label = Label.builder()
                .name("Test Label")
                .user(user)
                .build();
        label = labelRepository.save(label);


        Entry entry = Entry.builder()
                .title("Title01")
                .content("Content\n01")
                .label(label) // fk
                .user(user) // fk
                .build();

        // when
        Entry savedEntry = entryRepository.save(entry);

        // then
        Assertions.assertThat(savedEntry).isNotNull();
        Assertions.assertThat(savedEntry.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEntry.getUser()).isEqualTo(user);
        Assertions.assertThat(savedEntry.getLabel()).isEqualTo(label);
    }

    @Test
    public void Given_EntryWithoutLabel_When_Save_Then_ReturnEntry() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Entry entry = Entry.builder()
                .title("Title01")
                .content("Content01")
                .user(user)
                .build();

        // when
        Entry savedEntry = entryRepository.save(entry);

        // then
        Assertions.assertThat(savedEntry).isNotNull();
        Assertions.assertThat(savedEntry.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEntry.getUser()).isEqualTo(user);
        Assertions.assertThat(savedEntry.getLabel()).isNull();
    }
}
