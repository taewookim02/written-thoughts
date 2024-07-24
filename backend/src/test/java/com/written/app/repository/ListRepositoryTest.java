package com.written.app.repository;

import com.written.app.model.List;
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
public class ListRepositoryTest {

    @Autowired
    private ListRepository listRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void ListRepository_FindAllByUserId_ReturnLists() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);

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



}
