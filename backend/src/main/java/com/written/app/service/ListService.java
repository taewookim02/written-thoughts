package com.written.app.service;

import com.written.app.dto.ListDto;
import com.written.app.mapper.ListMapper;
import com.written.app.model.List;
import com.written.app.model.User;
import com.written.app.repository.ListRepository;
import com.written.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ListService {
    private final ListRepository listRepository;
    private final UserRepository userRepository;

    public ListService(ListRepository listRepository, UserRepository userRepository) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
    }

    public java.util.List<List> findAllByUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return listRepository.findAllByUserId(user.getId());
    }

    public ListDto create(ListDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with the id: " + dto.userId()));

        List list = List.builder()
                .title(dto.title())
                .user(user)
                .build();

        List save = listRepository.save(list);

        return ListMapper.toListDto(save);
    }

    public ListDto update(Integer listId, ListDto dto) {
        List list = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found with the id: " + dto.id()));

        list.setTitle(dto.title());
        List save = listRepository.save(list);

        return ListMapper.toListDto(save);
    }

    public void delete(Integer listId) {
        if (!listRepository.existsById(listId)) {
            throw new EntityNotFoundException("List not found with the id: " + listId);
        }
        listRepository.deleteById(listId);
    }
}
