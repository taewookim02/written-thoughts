package com.written.app.repository;

import com.written.app.model.List;
import com.written.app.model.ListItem;
import com.written.app.model.Role;
import com.written.app.model.User;
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
                .content("ListItem01")
                .list(list)
                .build();

        // when
        ListItem savedListItem = listItemRepository.save(listItem);

        // then
        assertThat(savedListItem).isNotNull();
        assertThat(savedListItem.getList()).isEqualTo(list);
        assertThat(savedListItem.getContent()).isEqualTo("ListItem01");
        assertThat(savedListItem.getList().getUser()).isEqualTo(user);
    }


    @Test
    public void ListItemRepository_FindById_ReturnListItem() {
        // given
        ListItem listItem = ListItem.builder()
                .list(list)
                .content("ListItem01")
                .build();
        listItemRepository.save(listItem);

        ListItem listItem2 = ListItem.builder()
                .list(list)
                .content("ListItem02")
                .build();
        listItemRepository.save(listItem2);

        // when
        ListItem resultListItem = listItemRepository.findById(listItem2.getId()).get();

        // then
        assertThat(resultListItem).isNotNull();
        assertThat(resultListItem).isNotEqualTo(listItem);
        assertThat(resultListItem.getId()).isEqualTo(listItem2.getId());
        assertThat(resultListItem.getContent()).isEqualTo("ListItem02");
    }

    @Test
    public void ListItemRepository_Update_ReturnListItem() {
        // given
        ListItem listItem = ListItem.builder()
                .list(list)
                .content("ListItem01")
                .build();
        listItemRepository.save(listItem);

        // when
        ListItem savedListItem = listItemRepository.findById(listItem.getId()).orElseThrow();
        savedListItem.setContent("Updated ListItem01");
        listItemRepository.save(savedListItem);


        // then
        ListItem updatedListItem = listItemRepository.findById(listItem.getId()).orElseThrow();
        assertThat(updatedListItem).isNotNull();
        assertThat(updatedListItem.getId()).isEqualTo(listItem.getId());
        assertThat(updatedListItem.getContent()).isEqualTo("Updated ListItem01");
        assertThat(updatedListItem.getList()).isEqualTo(list);
    }

    @Test
    public void ListItemRepository_Delete_RemoveListItem() {
        // given
        ListItem listItem = ListItem.builder()
                .content("ListItem to delete")
                .list(list)
                .build();
        listItemRepository.save(listItem);

        // when
        listItemRepository.deleteById(listItem.getId());

        // then
        Optional<ListItem> deletedListItem = listItemRepository.findById(listItem.getId());
        assertThat(deletedListItem).isEmpty();
    }

}
