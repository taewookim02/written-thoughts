package com.written.app.repository;

import com.written.app.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
}
