package com.written.app.service;

import com.written.app.dto.ListDto;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.ListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {

    @Mock
    private ListRepository listRepository;
    @InjectMocks
    private ListService listService;

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
    public void ListService_FindAllByUser_ReturnLists() {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        List<com.written.app.model.List> expectedEntries = Arrays.asList(
                com.written.app.model.List.builder().id(2).title("Entry 2").user(user).build(),
                com.written.app.model.List.builder().id(1).title("Entry 1").user(user).build()
        );
        when(listRepository.findAllByUserId(user.getId())).thenReturn(expectedEntries);

        // when
        List<com.written.app.model.List> resultLists = listService.findAllByUser(authToken);

        // then
        assertThat(resultLists).isNotNull();
        assertThat(resultLists).hasSize(2);
        assertThat(resultLists).isEqualTo(expectedEntries);

        verify(listRepository).findAllByUserId(user.getId());
    }

    @Test
    public void ListService_Create_ReturnListDto() {
        // given
        Integer listId = 1;
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        ListDto listDto = new ListDto(listId, user.getId(), "List01");

        com.written.app.model.List list = com.written.app.model.List.builder()
                .user(user)
                .title(listDto.title())
                .build();

        when(listRepository.save(any(com.written.app.model.List.class))).thenAnswer(invocation -> {
            com.written.app.model.List savedList = invocation.getArgument(0);
            savedList.setId(listId);
            return savedList;
        });

        // when
        ListDto resultDto = listService.create(listDto, authToken);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.title()).isEqualTo("List01");
        assertThat(resultDto.userId()).isEqualTo(listId);

        verify(listRepository).save(any(com.written.app.model.List.class));
    }

    @Test
    public void ListRepository_Update_ReturnListDto() throws AccessDeniedException {
        // given
        Integer listId = 1;
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        ListDto listDto = new ListDto(listId, user.getId(), "Updated List");


        com.written.app.model.List existingList = com.written.app.model.List.builder()
                .id(listId)
                .user(user)
                .title("Original List")
                .build();

        com.written.app.model.List updatedList = com.written.app.model.List.builder()
                .id(listId)
                .user(user)
                .title(listDto.title())
                .build();
        when(listRepository.findById(listId)).thenReturn(Optional.of(existingList));
        when(listRepository.save(any(com.written.app.model.List.class))).thenReturn(updatedList);

        // when
        ListDto updatedListDto = listService.update(listId, listDto, authToken);

        // then
        assertThat(updatedListDto).isNotNull();
        assertThat(updatedListDto.id()).isEqualTo(listId);
        assertThat(updatedListDto.userId()).isEqualTo(user.getId());
        assertThat(updatedListDto.title()).isEqualTo("Updated List");

        verify(listRepository).save(any(com.written.app.model.List.class));
    }

    @Test
    public void ListService_Delete_ReturnVoid() {
        // given
        Integer listId = 1;
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        com.written.app.model.List listToDelete = com.written.app.model.List.builder()
                .id(listId)
                .user(user)
                .title("List to delete")
                .build();

        when(listRepository.findById(listId)).thenReturn(Optional.of(listToDelete));

        // when
        Assertions.assertThatCode(() -> listService.delete(listId, authToken))
                .doesNotThrowAnyException();

        // then
        verify(listRepository).findById(listId);
        verify(listRepository).deleteById(listId);
    }

}
