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
    public List<LabelDto> findAllByUser(Principal connectedUser)  {
        return labelService.findAllByUser(connectedUser);
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
            @RequestBody LabelDto dto
    ) {
        // TODO: check if userId matches
        LabelDto updatedLabel = labelService.update(labelId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedLabel);
    }

}
