package com.written.app.controller;

import com.written.app.dto.LabelResponse;
import com.written.app.model.Label;
import com.written.app.service.LabelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/user-label/{user_id}")
    public List<LabelResponse> findAllByUserId(@PathVariable("user_id") Integer userId) {
        return labelService.findAllByUserId(userId);
    }
}
