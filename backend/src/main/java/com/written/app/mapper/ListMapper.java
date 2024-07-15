package com.written.app.mapper;

import com.written.app.dto.ListDto;
import com.written.app.model.List;

public class ListMapper {

    public static ListDto toListDto(List list) {
        return new ListDto(
                list.getId(),
                list.getUser().getId(),
                list.getTitle()
        );
    }
}
