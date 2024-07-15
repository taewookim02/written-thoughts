package com.written.app.dto;

public record ListItemDto(
        Integer id,
        String content,
        Integer listId
) {
}
