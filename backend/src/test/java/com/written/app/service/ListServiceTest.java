package com.written.app.service;

import com.written.app.dto.EntryDto;
import com.written.app.dto.ListDto;
import com.written.app.mapper.ListMapper;
import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
            savedList.setId(1);
            return savedList;
        });

        // when
        ListDto resultDto = listService.create(listDto, authToken);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.title()).isEqualTo("List01");
        assertThat(resultDto.userId()).isEqualTo(1);

        verify(listRepository).save(any(com.written.app.model.List.class));
    }


}
