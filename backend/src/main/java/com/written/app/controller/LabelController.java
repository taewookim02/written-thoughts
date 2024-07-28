package com.written.app.controller;

import com.written.app.dto.LabelDto;
import com.written.app.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/labels")
    public ResponseEntity<List<LabelDto>> findAllByUser(Principal connectedUser)  {
        List<LabelDto> labels = labelService.findAllByUser(connectedUser);
        return ResponseEntity.ok(labels);
    }

    @PostMapping("/labels")
    public ResponseEntity<LabelDto> create(@RequestBody LabelDto dto, Principal connectedUser) {

        LabelDto createdLabel = labelService.create(dto, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }

    @DeleteMapping("/labels/{label-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("label-id") Integer id,
                       Principal connectedUser) throws AccessDeniedException {
        labelService.delete(id, connectedUser);
    }

    @PatchMapping("/labels/{label-id}")
    public ResponseEntity<LabelDto> update(
            @PathVariable("label-id") Integer labelId,
            @RequestBody LabelDto dto,
            Principal connectedUser
    ) throws AccessDeniedException {
        LabelDto updatedLabel = labelService.update(labelId, dto, connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedLabel);
    }

}
