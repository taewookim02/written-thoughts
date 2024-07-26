package com.written.app.service;

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

}
