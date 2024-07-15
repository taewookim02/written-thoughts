package com.written.app.service;

import com.written.app.model.Entry;
import com.written.app.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public List<Entry> findAllByUserId(Integer userId) {
        return entryRepository.findAllByUserId(userId);
    }
}
