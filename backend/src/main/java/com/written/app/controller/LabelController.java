package com.written.app.controller;

import com.written.app.dto.LabelDto;
import com.written.app.service.LabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/user-label/{user_id}")
    public List<LabelDto> findAllByUserId(@PathVariable("user_id") Integer userId) {
        return labelService.findAllByUserId(userId);
    }

    @PostMapping
    public LabelDto create(@RequestBody LabelDto dto) {
        return labelService.create(dto);
    }
}
