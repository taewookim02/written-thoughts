package com.written.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Entry {
    @Id
    @GeneratedValue
    private Integer entryNo;


    private String title;

    @Column(
            length = 10000
    )
    private String content;

    private boolean isPrivate;

    @Column(
            updatable = false
    )
    private LocalDateTime createdAt;
}
