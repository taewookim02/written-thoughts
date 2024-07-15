package com.written.app.controller;

import com.written.app.dto.ListItemDto;
import com.written.app.model.List;
import com.written.app.model.ListItem;
import com.written.app.service.ListItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
