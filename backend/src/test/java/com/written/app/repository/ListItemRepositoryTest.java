package com.written.app.repository;

import com.written.app.model.List;
import com.written.app.model.ListItem;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ListItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListRepository listRepository;
    @Autowired
    private ListItemRepository listItemRepository;

    private User user;
    private List list;


    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        list = List.builder()
                .title("List01")
                .user(user)
                .build();
        listRepository.save(list);
    }


    @Test
    public void ListItemRepository_Save_ReturnListItem() {
        // given
        ListItem listItem = ListItem.builder()
                .content("List Item 01")
                .list(list)
                .build();

        // when
        ListItem savedListItem = listItemRepository.save(listItem);

        // then
        assertThat(savedListItem).isNotNull();
        assertThat(savedListItem.getList()).isEqualTo(list);
    }


}
