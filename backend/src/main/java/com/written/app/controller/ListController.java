package com.written.app.controller;

import com.written.app.dto.ListDto;
import com.written.app.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<List<com.written.app.model.List>> findAllByUser(
        Principal connectedUser
    ) {
        List<com.written.app.model.List> lists = listService.findAllByUser(connectedUser);
        return ResponseEntity.ok(lists);
    }

    @PostMapping("/lists")
    public ResponseEntity<ListDto> create(@RequestBody ListDto dto,
                          Principal connectedUser) {
        ListDto listDto = listService.create(dto, connectedUser);
        return ResponseEntity
                .created(URI.create("/lists/" + listDto.id()))
                .body(listDto);
    }

    @PatchMapping("/lists/{list-id}")
    public ResponseEntity<ListDto> update(
            @PathVariable("list-id") Integer listId,
            @RequestBody ListDto dto,
            Principal connectedUser
    ) throws AccessDeniedException {
        ListDto listDto = listService.update(listId, dto, connectedUser
        );
        return ResponseEntity.ok(listDto);
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
