package com.written.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
