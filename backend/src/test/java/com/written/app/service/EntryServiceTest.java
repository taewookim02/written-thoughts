package com.written.app.service;

import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.EntryRepository;
import com.written.app.repository.LabelRepository;
import org.assertj.core.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryServiceTest {
    @Mock
    private EntryRepository entryRepository;
    @Mock
    private LabelRepository labelRepository;
    @InjectMocks
    private EntryService entryService;

    private User user;
    private Entry entry;
    private Label label;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        label = Label.builder()
                .user(user)
                .name("Label01")
                .build();
        entry = Entry.builder()
                .title("Title01")
                .content("Content01")
                .user(user)
                .label(label)
                .build();

    }

    @Test
    public void EntryService_Create_ReturnEntry() {
        // given
        EntryDto dto = new EntryDto("Test title", "Test content", 1, true);

//        Principal mockPrincipal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);

//        when(mockPrincipal.getName()).thenReturn("test@example.com");
        when(authToken.getPrincipal()).thenReturn(user);

        Label mockLabel = Label.builder().id(1).name("Test label").user(user).build();

        when(labelRepository.findById(1)).thenReturn(Optional.of(mockLabel));
        when(entryRepository.save(any(Entry.class))).thenAnswer(invocation -> {
            Entry savedEntry = invocation.getArgument(0);
            savedEntry.setId(1);
            ;
            return savedEntry;
        });

        // when
        Entry result = entryService.create(dto, authToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test title");
        assertThat(result.getContent()).isEqualTo("Test content");
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getLabel()).isEqualTo(mockLabel);
        assertThat(result.isPublic()).isTrue();

        verify(labelRepository).findById(1);
        verify(entryRepository).save(any(Entry.class));

    }

    @Test
    public void EntryService_FindAllByUser_ReturnEntryList() {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        List<Entry> expectedEntries = Arrays.asList(
                Entry.builder().id(2).title("Entry 2").content("Content 2").user(user).build(),
                Entry.builder().id(1).title("Entry 1").content("Content 1").user(user).build()
        );

        when(entryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())).thenReturn(expectedEntries);

        // when
        List<Entry> result = entryService.findAllByUser(authToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedEntries);

        verify(entryRepository).findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Test
    public void EntryService_Delete_ReturnVoid() {
        // given
        Integer entryId = 1;
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);


        Entry entryToDelete = Entry.builder()
                .id(entryId)
                .title("Test entry to delte")
                .content("Test entry content")
                .user(user)
                .build();

        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entryToDelete));


        // when
        Assertions.assertThatCode(() -> entryService.delete(entryId, authToken))
                .doesNotThrowAnyException();

        // then
        verify(entryRepository).findById(entryId);
        verify(entryRepository).delete(entryToDelete);
    }

}
