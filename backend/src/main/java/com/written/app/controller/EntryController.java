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

import java.net.URI;
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
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(entry);
    }

    @GetMapping("/entry/user")
    public List<Entry> findAllByUser(
            Principal connectedUser
    ) {
        return entryService.findAllByUser(connectedUser);
    }

    @PostMapping("/entry")
    public ResponseEntity<Entry> create(@RequestBody EntryDto dto,
                        Principal connectedUser) {
        Entry savedEntry = entryService.create(dto, connectedUser);
        return ResponseEntity
                .created(URI.create("/entry/" + savedEntry.getId()))
                .body(savedEntry);
    }

    @DeleteMapping("/entry/{entry-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("entry-id") Integer entryId,
            Principal connectedUser
    ) throws AccessDeniedException {
        entryService.delete(entryId, connectedUser);
    }

    @PatchMapping("/entry/{entry-id}")
    public Entry update(
            @PathVariable("entry-id") Integer entryId,
            @RequestBody EntryDto dto,
            Principal connectedUser
    ) throws AccessDeniedException {
        return entryService.update(entryId, dto, connectedUser);
    }


    @GetMapping("/entry/download")
    public ResponseEntity<Resource> downloadEntries(Principal connectedUser) {

        String content = entryService.downloadEntries(connectedUser);

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
        // TODO: pagination for all public entries
        return entryService.findTop20ByIsPublicTrueOrderByCreatedAtDesc();
    }

}

