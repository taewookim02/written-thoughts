package com.written.app.service;


import com.written.app.model.Label;
import com.written.app.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> findAllByUserId(Integer userId) {
        return labelRepository.findAllByUserId(userId);
    }
}
