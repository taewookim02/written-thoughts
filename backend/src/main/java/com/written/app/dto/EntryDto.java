package com.written.app.dto;

public record EntryDto(
         String title,
         String content,
         Integer labelId,
         Integer userId

) {
}
