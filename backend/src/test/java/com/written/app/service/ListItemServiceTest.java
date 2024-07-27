package com.written.app.service;

import com.written.app.dto.ListItemDto;
import com.written.app.model.List;
import com.written.app.model.ListItem;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.ListItemRepository;
import com.written.app.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListItemServiceTest {

    @Mock
    private ListRepository listRepository;
    @Mock
    private ListItemRepository listItemRepository;
    @InjectMocks
    private ListItemService listItemService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1)
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void ListItemService_Create_ReturnListItemDto() throws AccessDeniedException {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        Integer listId = 1;
        Integer listItemId = 1;
        ListItemDto inputDto = new ListItemDto(null, "Test content", listId);
        List list = List.builder()
                .id(listId)
                .title("List01")
                .user(user)
                .build();
        when(listRepository.findById(listId)).thenReturn(Optional.of(list));

        ListItem savedListItem = ListItem.builder()
                .id(listItemId)
                .content("Test content")
                .list(list)
                .build();
        when(listItemRepository.save(any(ListItem.class))).thenReturn(savedListItem);


        // when
        ListItemDto resultDto = listItemService.create(inputDto, authToken);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.id()).isEqualTo(listItemId);
        assertThat(resultDto.listId()).isEqualTo(listId);
        assertThat(resultDto.content()).isEqualTo("Test content");

        verify(listRepository).findById(listId);
        verify(listItemRepository).save(any(ListItem.class));
    }

    @Test
    public void ListItemService_Update_ReturnListItemDto() throws AccessDeniedException {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        Integer listId = 1;
        Integer listItemId = 1;
        ListItemDto inputDto = new ListItemDto(listItemId, "Updated content", listId);

        List list = List.builder()
                .id(listId)
                .title("List01")
                .user(user)
                .build();

        ListItem savedListItem = ListItem.builder()
                .id(listItemId)
                .content("Test content")
                .list(list)
                .build();
        when(listItemRepository.findById(listItemId)).thenReturn(Optional.of(savedListItem));

        ListItem updatedListItem = ListItem.builder()
                .id(inputDto.id())
                .content(inputDto.content())
                .list(list)
                .build();
        when(listItemRepository.save(savedListItem)).thenReturn(updatedListItem);

        // when
        ListItemDto resultDto = listItemService.update(listItemId, inputDto, authToken);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.content()).isEqualTo(inputDto.content());
        assertThat(resultDto.id()).isEqualTo(listItemId);
        assertThat(resultDto.listId()).isEqualTo(listId);

        verify(listItemRepository).findById(listItemId);
        verify(listItemRepository).save(any(ListItem.class));
    }

    @Test
    public void ListItemService_Delete_ReturnVoid() throws AccessDeniedException {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        Integer listId = 1;
        Integer listItemId = 1;
        List list = List.builder()
                .id(listId)
                .title("List01")
                .user(user)
                .build();
        ListItem foundListItem = ListItem.builder()
                .id(listItemId)
                .list(list)
                .content("Found list item")
                .build();

        when(listItemRepository.findById(listItemId)).thenReturn(Optional.of(foundListItem));

        // when
        listItemService.delete(listItemId, authToken);

        // then
        verify(listItemRepository).findById(listItemId);
        verify(listItemRepository).delete(foundListItem);
    }


}
