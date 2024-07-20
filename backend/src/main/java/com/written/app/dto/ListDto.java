package com.written.app.dto;

public record ListDto(
        Integer id,
        // TODO: is userId needed?
        Integer userId,
        String title

) {
}
