package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@AllArgsConstructor
public class CommentDto implements Serializable {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String authorUsername;

    public String getCreatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        return createdAt.format(formatter);
    }
}