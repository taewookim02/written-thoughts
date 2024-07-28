package com.written.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.config.JwtAuthenticationFilter;
import com.written.app.dto.LabelDto;
import com.written.app.service.LabelService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = LabelController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class LabelControllerTest {

    @MockBean
    private LabelService labelService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void LabelController_FindAllByUser_ReturnLabelDtoList() throws Exception {
        // given
        Integer labelId = 1;
        Integer labelId2 = 2;
        Integer userId = 1;

        List<LabelDto> labels = Arrays.asList(
                new LabelDto(labelId, "Label 01", userId),
                new LabelDto(labelId2, "Label 02", userId)
        );

        Principal mockPrincipal = mock(Principal.class);
        when(labelService.findAllByUser(mockPrincipal)).thenReturn(labels);

        // when
        ResultActions response = mockMvc.perform(get("/labels")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Label 01"))
                .andExpect(jsonPath("$[1].name").value("Label 02"));

        verify(labelService).findAllByUser(mockPrincipal);
    }

    @Test
    public void LabelController_Create_ReturnLabelDto() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);
        LabelDto inputDto = new LabelDto(null, "Label to input", 1);
        LabelDto savedDto = new LabelDto(1, "Label to input", 1);
        when(labelService.create(inputDto, mockPrincipal)).thenReturn(savedDto);

        // when
        ResultActions response = mockMvc.perform(post("/labels")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(savedDto.name()))
                .andExpect(jsonPath("$.id").value(savedDto.id()));
        verify(labelService).create(inputDto, mockPrincipal);
    }

    @Test
    public void LabelController_Delete_ReturnNoContent() throws Exception {
        // given
        Integer labelId = 1;
        Principal mockPrincipal = mock(Principal.class);
        doNothing().when(labelService).delete(labelId, mockPrincipal);

        // when
        ResultActions response = mockMvc.perform(delete("/labels/{label-id}", labelId)
                .principal(mockPrincipal));

        // then
        response.andExpect(status().isNoContent());
        verify(labelService).delete(labelId, mockPrincipal);
    }

    @Test
    public void LabelController_Update_ReturnLabelDto() throws Exception {
        // given
        Integer labelId = 1;
        Principal mockPrincipal = mock(Principal.class);
        LabelDto inputDto = new LabelDto(null, "Updated Label", 1);
        LabelDto updateDto = new LabelDto(1, "Updated Label", 1);
        when(labelService.update(labelId, inputDto, mockPrincipal)).thenReturn(updateDto);

        // when
        ResultActions response = mockMvc.perform(patch("/labels/{label-id}", labelId)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(inputDto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(labelId))
                .andExpect(jsonPath("$.name").value("Updated Label"));
        verify(labelService).update(labelId, inputDto, mockPrincipal);
    }


}
