package com.written.app.service;

import com.written.app.dto.LabelDto;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.LabelRepository;
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
public class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;
    @InjectMocks
    private LabelService labelService;

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
    public void LabelService_FindAllByUser_ReturnLabelDtoList() {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        List<Label> expectedLabels = Arrays.asList(
                Label.builder().id(1).name("Label 1").user(user).build(),
                Label.builder().id(2).name("Label 2").user(user).build()
        );
        when(labelRepository.findAllByUserId(user.getId())).thenReturn(expectedLabels);


        // when
        List<LabelDto> result = labelService.findAllByUser(authToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).name()).isEqualTo("Label 1");
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(1).name()).isEqualTo("Label 2");

        verify(authToken).getPrincipal();
        verify(labelRepository).findAllByUserId(user.getId());
    }
}
