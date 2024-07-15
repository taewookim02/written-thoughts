package com.written.app.mapper;

import com.written.app.dto.LabelResponse;
import com.written.app.model.Label;
import org.springframework.stereotype.Service;

@Service
public class LabelMapper {
    public static LabelResponse toLabelResponseDto(Label label) {
        return new LabelResponse(label.getId(), label.getName());
    }
}
