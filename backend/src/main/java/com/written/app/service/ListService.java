package com.written.app.service;

import com.written.app.model.List;
import com.written.app.repository.ListRepository;
import org.springframework.stereotype.Service;

@Service
public class ListService {
    private final ListRepository listRepository;

    public ListService(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    public java.util.List<List> findAllByUserId(Integer userId) {
        return listRepository.findAllByUserId(userId);
    }
}
