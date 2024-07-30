package com.written.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.AuthenticationRequest;
import com.written.app.dto.AuthenticationResponse;
import com.written.app.dto.RegisterRequest;
import com.written.app.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthenticationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;


    @Test
    public void AuthenticationController_Register_ReturnAuthResponse() throws Exception {
        // given
        RegisterRequest inputRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken("jwtToken")
//                .refreshToken("refreshToken")
                .build();

        HttpServletResponse servletResponse = mock(HttpServletResponse.class);

        when(authenticationService.register(inputRequest, servletResponse)).thenReturn(authResponse);


        // when
        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputRequest)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("jwtToken"))
                .andExpect(jsonPath("$.refresh_token").value("refreshToken"));
        verify(authenticationService).register(inputRequest, servletResponse);
    }


    @Test
    public void AuthenticationController_Authenticate_ReturAuthResponse() throws Exception {
        // given
        AuthenticationRequest inputRequest = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken("jwtToken")
//                .refreshToken("refreshToken")
                .build();
        when(authenticationService.authenticate(inputRequest)).thenReturn(authResponse);

        // when
        ResultActions response = mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputRequest)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("jwtToken"))
                .andExpect(jsonPath("$.refresh_token").value("refreshToken"));
        verify(authenticationService).authenticate(inputRequest);
    }

    @Test
    public void AuthenticationController_RefreshToken_ReturnAuthResponse() throws Exception {
        // given
        String refreshToken = "validRefreshToken";
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken("newAccessToken")
//                .refreshToken("refreshToken")
                .build();

        // mock the behavior of `AuthenticationService.refreshToken(req, res)`
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            return null;
        }).when(authenticationService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when
        ResultActions response = mockMvc.perform(post("/auth/refresh-token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token").value("newAccessToken"))
                .andExpect(jsonPath("$.refresh_token").value("newRefreshToken"));
        verify(authenticationService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

    }

}
