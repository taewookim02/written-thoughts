package com.written.app.service;

import com.written.app.dto.ListItemDto;
import com.written.app.mapper.ListItemMapper;
import com.written.app.model.List;
import com.written.app.model.ListItem;
import com.written.app.repository.ListItemRepository;
import com.written.app.repository.ListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ListItemService {
    private final ListItemRepository listItemRepository;
    private final ListRepository listRepository;

    public ListItemService(ListItemRepository listItemRepository, ListRepository listRepository) {
        this.listItemRepository = listItemRepository;
        this.listRepository = listRepository;
    }

    public ListItemDto create(ListItemDto dto) {
        List list = listRepository.findById(dto.listId())
                .orElseThrow(() -> new EntityNotFoundException("List not found with the id: " + dto.listId()));

        ListItem listItem = ListItem.builder()
                .content(dto.content())
                .list(list)
                .build();
        ListItem save = listItemRepository.save(listItem);

        return ListItemMapper.toListItemDto(save);
    }

    public ListItemDto update(Integer listItemId, ListItemDto dto) {
        ListItem listItem = listItemRepository.findById(listItemId)
                .orElseThrow(() -> new EntityNotFoundException("List item not found with the id: " + listItemId));

        listItem.setContent(dto.content());
        ListItem save = listItemRepository.save(listItem);

        return ListItemMapper.toListItemDto(save);
    }

    public void delete(Integer listItemId) {
        if (!listItemRepository.existsById(listItemId)) {
            throw new EntityNotFoundException("List item not found with the id: " + listItemId);
        }
        listItemRepository.deleteById(listItemId);
    }
}
