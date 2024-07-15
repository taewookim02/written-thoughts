package com.written.app.service;


import com.written.app.dto.LabelResponse;
import com.written.app.mapper.LabelMapper;
import com.written.app.model.Label;
import com.written.app.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<LabelResponse> findAllByUserId(Integer userId) {
        return labelRepository.findAllByUserId(userId)
                .stream()
                .map(LabelMapper::toLabelResponseDto)
                .collect(Collectors.toList());

    }
}
