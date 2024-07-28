package com.written.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.EntryDto;
import com.written.app.dto.ListDto;
import com.written.app.dto.ListItemDto;
import com.written.app.model.Entry;
import com.written.app.model.ListItem;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.service.ListItemService;
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

import java.nio.file.AccessDeniedException;
import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ListItemController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ListItemControllerTest {

    @MockBean
    private ListItemService listItemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private Entry entry;
    private User user;
    private EntryDto entryDto;
    private ListDto listDto;
    private com.written.app.model.List list;
    private ListItemDto listItemDto;
    private ListItemDto listItemInputDto;

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
        listItemInputDto = new ListItemDto(null, "List item 01", 1);
        listItemDto = new ListItemDto(1, "List item 01", 1);
    }

    @Test
    public void ListItemService_Create_ReturnListItemDto() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        when(listItemService.create(listItemInputDto, mockPrincipal)).thenReturn(listItemDto);

        // when
        ResultActions response = mockMvc.perform(post("/list-items")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listItemInputDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(listItemDto.id()))
                .andExpect(jsonPath("$.content").value(listItemDto.content()));
        verify(listItemService).create(listItemInputDto, mockPrincipal);
    }

    @Test
    public void ListItemService_Update_ReturnListItemDto() throws Exception {
        // given
        Integer listItemId = 1;
        Integer listId = 1;
        Principal mockPrincipal = mock(Principal.class);
        ListItemDto updateRequest = new ListItemDto(null, "Updated Content", listId);
        ListItemDto updatedListItem = new ListItemDto(listItemId, "Updated Content", listId);
        when(listItemService.update(listItemId, updateRequest, mockPrincipal)).thenReturn(updatedListItem);

        // when
        ResultActions response = mockMvc.perform(patch("/list-items/{list-item-id}", listItemId)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(listItemDto.id()))
                .andExpect(jsonPath("$.content").value(updatedListItem.content()));

        verify(listItemService).update(listItemId, updateRequest, mockPrincipal);
    }
}
