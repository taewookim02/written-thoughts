package com.written.app.controller;

import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.service.EntryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("/entry/{entry-id}")
    public ResponseEntity<Entry> findById(
            @PathVariable("entry-id") Integer entryId,
            Principal connectedUser
    ) throws AccessDeniedException {
        Entry entry = entryService.findById(entryId, connectedUser);
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/entry/user")
    public List<Entry> findAllByUser(
            Principal connectedUser
    ) {
        return entryService.findAllByUser(connectedUser);
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

    @PatchMapping("/entry/{entry-id}")
    public Entry update(
            @PathVariable("entry-id") Integer entryId,
            @RequestBody EntryDto dto
    ) {
        // TODO: check if userId matches
        return entryService.update(entryId, dto);
    }


    @GetMapping("/entry/download/{user-id}")
    public ResponseEntity<Resource> downloadEntries(@PathVariable("user-id") Integer userId) {
        // TODO: check if userId matches
        String content = entryService.downloadEntries(userId);

        ByteArrayResource resource = new ByteArrayResource(content.getBytes());

        // download entries (entries.txt)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=entries.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }


    // get top 20 public entries order by date descending
    @GetMapping("/entry/public")
    public List<Entry> findTop20ByIsPublicTrueOrderByCreatedAtDesc() {
        return entryService.findTop20ByIsPublicTrueOrderByCreatedAtDesc();
    }

}
