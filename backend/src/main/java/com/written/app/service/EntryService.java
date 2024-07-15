package com.written.app.service;

import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.User;
import com.written.app.repository.EntryRepository;
import com.written.app.repository.LabelRepository;
import com.written.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    private final EntryRepository entryRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    public EntryService(EntryRepository entryRepository, LabelRepository labelRepository, UserRepository userRepository) {
        this.entryRepository = entryRepository;
        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
    }

    public List<Entry> findAllByUserId(Integer userId) {
        return entryRepository.findAllByUserId(userId);
    }

    public Entry create(EntryDto dto) {
        Entry entry = new Entry();
        entry.setTitle(dto.title());
        entry.setContent(dto.content());

        // set label if exists
        if (dto.labelId() != null) {
            Label label = labelRepository.findById(dto.labelId())
                    .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + dto.labelId()));
            entry.setLabel(label);
        }

        // set user
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.userId()));
        entry.setUser(user);

        return entryRepository.save(entry);
    }

    public void delete(Integer entryId) {
        // TODO: check if entry was deleted
        entryRepository.deleteById(entryId);
    }

    public Entry update(Integer entryId, EntryDto dto) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found with the id: " + entryId));

        // update title if provided
        if (dto.title() != null) {
            entry.setTitle(dto.title());
        }

        // update content if provided
        if (dto.content() != null) {
            entry.setContent(dto.content());
        }

        // update isPrivate if provided
        if (dto.isPrivate() != null) {
            entry.setPrivate(dto.isPrivate());
        }

        // update labelId if provided
        if (dto.labelId() != null) {
            Label label = labelRepository.findById(dto.labelId())
                    .orElseThrow(() -> new EntityNotFoundException("Label not found with the id: " + dto.labelId()));
            entry.setLabel(label);
        }


        return entryRepository.save(entry);

    }

    public Entry findById(Integer entryId) {
        return entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found with the id: " + entryId));
    }

    public String downloadEntries(Integer userId) {
        List<Entry> entries = entryRepository.findAllByUserId(userId);

        StringBuilder content = new StringBuilder();
        for (Entry entry : entries) {
            content.append("----------------\n");
            content.append("Title: ").append(entry.getTitle()).append("\n");
            content.append("Date: ").append(entry.getCreatedAt()).append("\n");
            content.append("Content: ").append(entry.getContent()).append("\n");
            content.append("----------------\n\n");
        }

        return content.toString();
    }
}
