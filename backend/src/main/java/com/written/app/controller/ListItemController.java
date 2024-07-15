package com.written.app.controller;

import com.written.app.dto.ListItemDto;
import com.written.app.service.ListItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ListItemController {

    private final ListItemService listItemService;

    public ListItemController(ListItemService listItemService) {
        this.listItemService = listItemService;
    }

    @PostMapping("/list-items")
    public ListItemDto create(@RequestBody ListItemDto dto) {
        return listItemService.create(dto);
    }

    @PatchMapping("/list-items/{list-item-id}")
    public ListItemDto update(
            @PathVariable("list-item-id") Integer listItemId,
            @RequestBody ListItemDto dto
    ) {
        // TODO: check if userId matches
        return listItemService.update(listItemId, dto);
    }

    @DeleteMapping("/list-items/{list-item-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("list-item-id") Integer listItemId) {
        listItemService.delete(listItemId);
    }
}
