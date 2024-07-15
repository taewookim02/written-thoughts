package com.written.app.repository;

import com.written.app.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
    List<Entry> findAllByUserId(Integer userId);

    List<Entry> findTop20ByIsPublicTrueOrderByCreatedAtDesc();

//    TODO: add ordered entry list fetching
//    findAllByUserIdOrderByCreatedAtDesc
}
 