package com.written.app.controller;

import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        // TODO: check if userId matches
        return entryService.findAllByUserId(userId);
    }

    @PostMapping("/entry")
    public Entry create(@RequestBody EntryDto dto) {
        System.out.println("dto = " + dto);
        return entryService.create(dto);
    }

    @DeleteMapping("/entry/{entry-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("entry-id") Integer entryId
    ) {
        // TODO: check if userId matches
        entryService.delete(entryId);
    }

}
