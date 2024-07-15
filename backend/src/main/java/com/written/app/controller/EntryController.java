package com.written.app.controller;

import com.written.app.model.Entry;
import com.written.app.service.EntryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("/entry/{user-id}")
    public List<Entry> findAllByUserId(
            @PathVariable("user-id") Integer userId
    ) {
        return entryService.findAllByUserId(userId);

    }


}
