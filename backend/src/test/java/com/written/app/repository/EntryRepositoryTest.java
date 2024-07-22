package com.written.app.repository;

import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // default: rollback each transaction after method ends
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EntryRepositoryTest {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    // declare testUser as class field for use
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void EntryRepository_Save_ReturnEntry() {
        // given
        Label label = Label.builder()
                .name("Test Label")
                .user(testUser)
                .build();
        label = labelRepository.save(label);


        Entry entry = Entry.builder()
                .title("Title01")
                .content("Content\n01")
                .label(label) // fk
                .user(testUser) // fk
                .build();

        // when
        Entry savedEntry = entryRepository.save(entry);

        // then
        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isGreaterThan(0);
        assertThat(savedEntry.getUser()).isEqualTo(testUser);
        assertThat(savedEntry.getLabel()).isEqualTo(label);
    }


    @Test
    public void EntryRepository_SaveWithoutLabel_ReturnEntry() {
        // given
        Entry entry = Entry.builder()
                .title("Title01")
                .content("Content01")
                .user(testUser)
                .build();

        // when
        Entry savedEntry = entryRepository.save(entry);

        // then
        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isGreaterThan(0);
        assertThat(savedEntry.getUser()).isEqualTo(testUser);
        assertThat(savedEntry.getLabel()).isNull();
    }

    @Test
    public void EntryRepository_FindAllByUserIdOrderByCreatedAtDesc_ReturnEntryList() {
        // given

        // when

        // then
    }

    @Test
    public void EntryRepository_FindById_ReturnEntry() {
        // given

        // when

        // then
    }


}
