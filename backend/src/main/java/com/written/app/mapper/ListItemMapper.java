package com.written.app.mapper;

import com.written.app.dto.ListItemDto;
import com.written.app.model.ListItem;

public class ListItemMapper {

    public static ListItemDto toListItemDto(ListItem listItem) {
        return new ListItemDto(
                listItem.getId(),
                listItem.getContent(),
                listItem.getList().getId()
        );
    }
}
