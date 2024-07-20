package com.written.app.service;

import com.written.app.dto.EntryDto;
import com.written.app.model.Entry;
import com.written.app.model.Label;
import com.written.app.model.User;
import com.written.app.repository.EntryRepository;
import com.written.app.repository.LabelRepository;
import com.written.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

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

    public List<Entry> findAllByUser(Principal connectedUser) {
        // get connected user
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // get entries of connected user
        return entryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Entry create(EntryDto dto, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Entry entry = new Entry();

        entry.setTitle(dto.title());
        entry.setContent(dto.content());

        // set label if exists
        if (dto.labelId() != null) {
            Label label = labelRepository.findById(dto.labelId())
                    .orElseThrow(() -> new EntityNotFoundException("Label not found with id: " + dto.labelId()));
            entry.setLabel(label);
        }

        // set isPublic if exists
        if (dto.isPublic() != null) {
            entry.setPublic(dto.isPublic());
        }

        // set user
        entry.setUser(user);

        return entryRepository.save(entry);
    }

    public void delete(Integer entryId, Principal connectedUser) throws AccessDeniedException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found with id: " + entryId));

        if (!Objects.equals(entry.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("User is not authorized to access this entry");
        }

//        entryRepository.deleteById(entryId);
        entryRepository.delete(entry);
    }


    public Entry update(Integer entryId, EntryDto dto, Principal connectedUser) throws AccessDeniedException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found with the id: " + entryId));

        if (!Objects.equals(user.getId(), entry.getUser().getId())) {
            throw new AccessDeniedException("User is not authorized to access this entry");
        }

        // update title if provided
        if (dto.title() != null) {
            entry.setTitle(dto.title());
        }

        // update content if provided
        if (dto.content() != null) {
            entry.setContent(dto.content());
        }

        // update isPublic if provided
        if (dto.isPublic() != null) {
            entry.setPublic(dto.isPublic());
        }

        // update labelId if provided
        if (dto.labelId() != null) {
            Label label = labelRepository.findById(dto.labelId())
                    .orElseThrow(() -> new EntityNotFoundException("Label not found with the id: " + dto.labelId()));
            entry.setLabel(label);
        }


        return entryRepository.save(entry);

    }

    public Entry findById(Integer entryId, Principal connectedUser) throws AccessDeniedException {
        // get user from principal
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // get entry with id
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found with the id: " + entryId));

        // only allow user to access their own resource
        if (!Objects.equals(entry.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("User is not authorized to access this entry");
        }

        return entry;
    }

    public String downloadEntries(Principal connectedUser) {
        // get user from principal
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // find all entries of connected user (order by created_at desc)
        List<Entry> entries = entryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        StringBuilder content = new StringBuilder();

        // append entries and return
        for (Entry entry : entries) {
            content.append("----------------\n");
            content.append("Title: ").append(entry.getTitle()).append("\n");
            content.append("Date: ").append(entry.getCreatedAt()).append("\n");
            content.append("Content: ").append(entry.getContent()).append("\n");
            content.append("----------------\n\n\n");
        }
        return content.toString();
    }

    public List<Entry> findTop20ByIsPublicTrueOrderByCreatedAtDesc() {
        return entryRepository.findTop20ByIsPublicTrueOrderByCreatedAtDesc();
    }
}
