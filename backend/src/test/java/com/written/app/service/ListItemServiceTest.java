package com.written.app.service;

import com.written.app.repository.ListItemRepository;
import com.written.app.repository.ListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListItemServiceTest {

    @Mock
    private ListRepository listRepository;
    @Mock
    private ListItemRepository listItemRepository;
    @InjectMocks
    private ListItemService listItemService;

    @Test
    public void ListItemService_Create_ReturnListItemDto() {

    }



}
