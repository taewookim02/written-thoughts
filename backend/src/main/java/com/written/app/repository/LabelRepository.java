package com.written.app.repository;

import com.written.app.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    List<Label> findAllByUserId(Integer userId);
}
