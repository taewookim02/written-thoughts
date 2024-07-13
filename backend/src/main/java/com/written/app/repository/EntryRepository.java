package com.written.app.repository;

import com.written.app.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
}
