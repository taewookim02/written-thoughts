package com.written.app.dto;

public record ListItemDto(
        // FIXME: duplicate list-item-id?
        Integer id,
        String content,
        Integer listId
) {
}
