package com.written.app.mapper;

import com.written.app.dto.LabelDto;
import com.written.app.model.Label;
import org.springframework.stereotype.Service;

@Service
public class LabelMapper {
    public static LabelDto toLabelDto(Label label) {
        return new LabelDto(
                label.getId(),
                label.getName(),
                label.getUser().getId()
        );
    }
}
