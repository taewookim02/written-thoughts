package com.written.app.repository;

import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

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

    @Test
    public void EntryRepository_Save_ReturnEntry() {
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        // given
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
        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isGreaterThan(0);
        assertThat(savedEntry.getUser()).isEqualTo(user);
        assertThat(savedEntry.getLabel()).isEqualTo(label);
    }


    @Test
    public void EntryRepository_SaveWithoutLabel_ReturnEntry() {
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
        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isGreaterThan(0);
        assertThat(savedEntry.getUser()).isEqualTo(user);
        assertThat(savedEntry.getLabel()).isNull();
    }

    @Test
    public void EntryRepository_FindAllByUserIdOrderByCreatedAtDesc_ReturnEntryList() throws InterruptedException {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Entry olderEntry = Entry.builder()
                .title("Title01")
                .content("Content01")
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        olderEntry = entryRepository.save(olderEntry);

        Thread.sleep(10);

        Entry newerEntry = Entry.builder()
                .title("Title02")
                .content("Content02")
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        newerEntry = entryRepository.save(newerEntry);

        // when
        List<Entry> entries = entryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        // then
        assertThat(entries).isNotNull();
        assertThat(entries).hasSize(2);
        assertThat(entries).isSortedAccordingTo(Comparator.comparing(Entry::getCreatedAt).reversed());
        assertThat(entries).containsExactlyInAnyOrder(olderEntry, newerEntry);
        assertThat(entries.get(0).getCreatedAt()).isAfterOrEqualTo(entries.get(1).getCreatedAt());
    }

    @Test
    public void EntryRepository_FindById_ReturnEntry() {
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
                .createdAt(LocalDateTime.now())
                .build();
        entry = entryRepository.save(entry);

        Entry entry2 = Entry.builder()
                .title("Title02")
                .content("Content02")
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        entry2 = entryRepository.save(entry2);

        System.out.println("entry = " + entry);
        System.out.println("entry2 = " + entry2);

        // when
        Entry resultEntry = entryRepository.findById(entry2.getId()).get();

        // then
        assertThat(resultEntry).isNotNull();
        assertThat(resultEntry.getUser()).isEqualTo(user);
        assertThat(resultEntry).isEqualTo(entry2);
        assertThat(resultEntry.getId()).isEqualTo(entry2.getId());
        assertThat(resultEntry.getTitle()).isEqualTo("Title02");
    }


    @Test
    public void EntryRepository_Delete_ReturnNull() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Entry entry = Entry.builder()
                .title("Title01")
                .content("Content01")
                .user(user)
                .build();
        entry = entryRepository.save(entry);

        // when
        entryRepository.delete(entry);

        // then
        assertThat(entryRepository.count()).isZero();
    }

}
