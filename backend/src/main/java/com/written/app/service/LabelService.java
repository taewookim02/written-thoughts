package com.written.app.service;


import com.written.app.dto.LabelDto;
import com.written.app.mapper.LabelMapper;
import com.written.app.model.Label;
import com.written.app.model.User;
import com.written.app.repository.LabelRepository;
import com.written.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LabelService {

    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    public LabelService(LabelRepository labelRepository, UserRepository userRepository) {
        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
    }

    public List<LabelDto> findAllByUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return labelRepository.findAllByUserId(user.getId())
                .stream()
                .map(LabelMapper::toLabelDto)
                .collect(Collectors.toList());

    }

    public LabelDto create(LabelDto dto, Principal connectedUser) {
        var user = (User) (((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());

        Label label = Label.builder()
                .name(dto.name())
                .user(user)
                .build();

        Label save = labelRepository.save(label);
        return LabelMapper.toLabelDto(save);
    }

    public void delete(Integer id, Principal connectedUser) throws AccessDeniedException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found with the id: " + id));

        if (!Objects.equals(user.getId(), label.getUser().getId())) {
            throw new AccessDeniedException("User is not authorized to access this label");
        }

        labelRepository.delete(label);
    }

    public LabelDto update(Integer id, LabelDto dto, Principal connectedUser) throws AccessDeniedException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));

        if (!Objects.equals(user.getId(), label.getUser().getId())) {
            throw new AccessDeniedException("User is not authorized to access this label");
        }

        label.setName(dto.name());

        Label save = labelRepository.save(label);
        return LabelMapper.toLabelDto(save);
    }
}
