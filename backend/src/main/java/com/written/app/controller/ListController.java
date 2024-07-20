package com.written.app.controller;

import com.written.app.dto.ListDto;
import com.written.app.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/lists")
    public List<com.written.app.model.List> findAllByUser(
        Principal connectedUser
    ) {
        return listService.findAllByUser(connectedUser);
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

    @DeleteMapping("/lists/{list-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("list-id") Integer listId
    ) {
        // TODO: check if userId matches
        listService.delete(listId);
    }
}
