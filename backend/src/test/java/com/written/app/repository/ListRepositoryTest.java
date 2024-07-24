package com.written.app.repository;

import com.written.app.model.List;
import com.written.app.model.Role;
import com.written.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ListRepositoryTest {

    @Autowired
    private ListRepository listRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
    }


    @Test
    public void ListRepository_FindAllByUserId_ReturnLists() {
        // given
        List list = List.builder()
                .title("List01")
                .user(user)
                .build();
        list = listRepository.save(list);

        List list2 = List.builder()
                .title("List01")
                .user(user)
                .build();
        list2 = listRepository.save(list2);

        // when
        java.util.List<List> resultLists = listRepository.findAllByUserId(user.getId());

        // then
        assertThat(resultLists).isNotNull();
        assertThat(resultLists.size()).isEqualTo(2);
        assertThat(resultLists).contains(list, list2);
    }

    @Test
    public void ListRepository_Save_ReturnList() {
        // given
        List list = List.builder()
                .title("List01")
                .user(user)
                .build();

        // when
        List savedList = listRepository.save(list);

        // then
        assertThat(savedList).isNotNull();
        assertThat(savedList.getUser()).isEqualTo(user);
        assertThat(savedList.getTitle()).isEqualTo("List01");
        assertThat(savedList.getId()).isEqualTo(list.getId());
    }

    @Test
    public void ListRepository_DeleteById_ReturnNull() {
        // given
        List list = List.builder()
                .title("List01")
                .user(user)
                .build();
        listRepository.save(list);

        List list2 = List.builder()
                .title("List02")
                .user(user)
                .build();
        listRepository.save(list2);

        Integer list1Id = list.getId();
        Integer list2Id = list2.getId();


        // when
        listRepository.deleteById(list.getId());

        // then
        assertThat(listRepository.count()).isOne();
        assertThat(listRepository.findById(list1Id)).isEmpty();
        assertThat(listRepository.findById(list2Id)).isPresent();
        assertThat(listRepository.findById(list2Id).get().getTitle()).isEqualTo("List02");
    }


}
