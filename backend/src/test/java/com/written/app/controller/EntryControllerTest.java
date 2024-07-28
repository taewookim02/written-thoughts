package com.written.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.service.EntryService;
import com.written.app.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = EntryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EntryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntryService entryService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;

    private Entry entry;
    private User user;
    private EntryDto entryDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        entry = Entry.builder()
                .id(1)
                .title("Entry01")
                .content("Content01")
                .user(user)
                .build();

        entryDto = new EntryDto("Entry01", "Content01", null, false);
    }


    @Test
    public void EntryController_FindById_ReturnEntry() throws Exception {
        // given
        Integer entryId = 1;
        when(entryService.findById(eq(entryId), any())).thenReturn(entry);

        // when
        ResultActions response = mockMvc.perform(get("/entry/1")
                .contentType(MediaType.APPLICATION_JSON));


        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entry.getId()))
                .andExpect(jsonPath("$.title").value(entry.getTitle()))
                .andExpect(jsonPath("$.content").value(entry.getContent()))
        ;
    }

    @Test
    public void EntryController_FindAllByUser_ReturnEntryList() throws Exception {
        // given
        List<Entry> entries = Arrays.asList(
                Entry.builder().id(1).title("Entry01").content("Content01").user(user).build(),
                Entry.builder().id(2).title("Entry02").content("Content02").user(user).build()
        );

        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");

        when(entryService.findAllByUser(any(Principal.class))).thenReturn(entries);

        // when
        ResultActions response = mockMvc.perform(get("/entry/user")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(entries.get(0).getId()))
                .andExpect(jsonPath("$[0].content").value(entries.get(0).getContent()))
                .andExpect(jsonPath("$[1].content").value(entries.get(1).getContent()));
    }

    @Test
    public void EntryController_Create_ReturnEntry() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");

        when(entryService.create(entryDto, mockPrincipal)).thenReturn(entry);

        // when
        ResultActions response = mockMvc.perform(post("/entry")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(entry.getContent()))
                .andExpect(jsonPath("$.id").value(entry.getId()))
                .andExpect(jsonPath("$.title").value(entry.getTitle()));
    }

    @Test
    public void EntryController_Delete_ReturnOk() throws Exception {
        // given
        Integer entryId = 1;
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");

        doNothing().when(entryService).delete(eq(entryId), any(Principal.class));

        // when
        ResultActions response = mockMvc.perform(delete("/entry/{entry-id}", entryId)
                .principal(mockPrincipal));

        // then
        response.andDo(print())
                .andExpect(status().isNoContent());
        verify(entryService).delete(eq(entryId), any(Principal.class));
    }

    @Test
    public void EntryController_Update_ReturnEntry() throws Exception {
        // given
        Integer entryId = 1;
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");
        when(entryService.update(entryId, entryDto, mockPrincipal)).thenReturn(entry);

        // when
        ResultActions response = mockMvc.perform(patch("/entry/{entry-id}", entryId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryDto)));

        // then
        response.andDo(print())
                .andExpect(jsonPath("$.title").value(entry.getTitle()))
                .andExpect(jsonPath("$.content").value(entry.getContent()))
                .andExpect(jsonPath("$.id").value(entry.getId()));
    }

    @Test
    public void EntryController_DownloadEntries_ReturnResource() throws Exception {
        // given
        String content = "Entry 1\nEntry 2\nEntry 3";
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");
        when(entryService.downloadEntries(mockPrincipal)).thenReturn(content);

        // when
        ResultActions response = mockMvc.perform(get("/entry/download")
                .principal(mockPrincipal));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=entries.txt"))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(content));

        verify(entryService).downloadEntries(mockPrincipal);
    }
}
