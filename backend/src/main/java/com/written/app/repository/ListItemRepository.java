package com.written.app.repository;

import com.written.app.model.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListItemRepository extends JpaRepository<ListItem, Integer> {
}
