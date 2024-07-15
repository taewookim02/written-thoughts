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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        entryRepository.deleteById(entryId);
    }
}
