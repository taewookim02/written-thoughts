package com.written.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.ChangePasswordRequestDto;
import com.written.app.dto.EntryDto;
import com.written.app.dto.ListDto;
import com.written.app.dto.ListItemDto;
import com.written.app.model.Entry;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.service.UserService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
    public void UserController_Delete_ReturnNoContent() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        doNothing().when(userService).delete(mockPrincipal);

        // when
        ResultActions response = mockMvc.perform(delete("/users")
                .principal(mockPrincipal));

        // then
        response.andExpect(status().isNoContent());
        verify(userService).delete(mockPrincipal);
    }

    @Test
    public void UserController_ChangePassword_ReturnOk() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        ChangePasswordRequestDto inputDto = ChangePasswordRequestDto.builder()
                .currentPassword("password")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();
        doNothing().when(userService).changePassword(inputDto, mockPrincipal);

        // when
        ResultActions response = mockMvc.perform(patch("/users/password")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(userService).changePassword(inputDto, mockPrincipal);
    }


}
