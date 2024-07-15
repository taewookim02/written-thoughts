package com.written.app.controller;

import com.written.app.dto.ListDto;
import com.written.app.service.ListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    // TODO: user-id from jwt or ?
    @GetMapping("/lists/{user-id}")
    // this currently returns list-items too
    // because of jpa bidirectional association
    public List<com.written.app.model.List> findAllByUserId(
            @PathVariable("user-id") Integer userId
    ) {
        // TODO: check if userId matches
        return listService.findAllByUserId(userId);
    }

    @PostMapping("/lists")
    public ListDto create(@RequestBody ListDto dto) {
        return listService.create(dto);
    }

    @PatchMapping("/lists/{list-id}")
    // TODO: is including userId appropriate? >> (ListDto)
    public ListDto update(
            @PathVariable("list-id") Integer listId,
            @RequestBody ListDto dto
    ) {
        // TODO: check if userId matches
        return listService.update(listId, dto);
    }
}
