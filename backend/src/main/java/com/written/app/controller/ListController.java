package com.written.app.controller;

import com.written.app.dto.ListDto;
import com.written.app.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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
    public ListDto create(@RequestBody ListDto dto,
                          Principal connectedUser) {
        return listService.create(dto, connectedUser);
    }

    @PatchMapping("/lists/{list-id}")
    public ListDto update(
            @PathVariable("list-id") Integer listId,
            @RequestBody ListDto dto,
            Principal connectedUser
    ) throws AccessDeniedException {
        return listService.update(listId, dto, connectedUser
        );
    }

    @DeleteMapping("/lists/{list-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("list-id") Integer listId,
            Principal connectedUser
    ) throws AccessDeniedException {
        listService.delete(listId, connectedUser);
    }
}
