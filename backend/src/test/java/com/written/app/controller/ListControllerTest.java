package com.written.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.EntryDto;
import com.written.app.dto.ListDto;
import com.written.app.model.Entry;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.service.ListService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ListController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ListControllerTest {
    @MockBean
    private ListService listService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    private Entry entry;
    private User user;
    private EntryDto entryDto;
    private ListDto listDto;
    private com.written.app.model.List list;

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

        list = com.written.app.model.List.builder()
                .title("List01")
                .id(1)
                .user(user)
                .build();

        entryDto = new EntryDto("Entry01", "Content01", null, false);
        listDto = new ListDto(1, 1, "List01");
    }

    @Test
    public void ListController_FindAllByUser_ReturnLists() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("test@example.com");

        List<com.written.app.model.List> lists = Arrays.asList(
                com.written.app.model.List.builder().id(1).user(user).title("List01").build(),
                com.written.app.model.List.builder().id(2).user(user).title("List02").build()
        );

        when(listService.findAllByUser(mockPrincipal)).thenReturn(lists);

        // when
        ResultActions response = mockMvc.perform(get("/lists")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(lists.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(lists.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(lists.get(1).getTitle()))
        ;
    }


    @Test
    public void ListController_Create_ReturnListDto() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        when(listService.create(listDto, mockPrincipal)).thenReturn(listDto);

        // when
        ResultActions response = mockMvc.perform(post("/lists")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(listDto.id()))
                .andExpect(jsonPath("$.title").value(listDto.title()));
        verify(listService).create(listDto, mockPrincipal);
    }

    @Test
    public void ListController_Update_ReturnListDto() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        Integer listId = 1;

        when(listService.update(listId, listDto, mockPrincipal)).thenReturn(listDto);

        // when
        ResultActions response = mockMvc.perform(patch("/lists/{list-id}", listId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(listDto.title()))
                .andExpect(jsonPath("$.id").value(listDto.id()));
        verify(listService).update(eq(listId), any(ListDto.class), eq(mockPrincipal));
    }

    @Test
    public void ListController_Delete_ReturnNoContent() throws Exception {
        // given
        Integer listId = 1;
        Principal mockPrincipal = mock(Principal.class);
        doNothing().when(listService).delete(listId, mockPrincipal);

        // when
        ResultActions response = mockMvc.perform(delete("/lists/{list-id}", listId)
                .principal(mockPrincipal));

        // then
        response.andDo(print())
                .andExpect(status().isNoContent());
        verify(listService).delete(listId, mockPrincipal);

    }

}
