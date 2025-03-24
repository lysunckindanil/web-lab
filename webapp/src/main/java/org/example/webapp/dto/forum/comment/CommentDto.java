package org.example.webapp.dto.forum.comment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
public class CommentDto implements Serializable {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String authorUsername;
    private Boolean canDelete;

    public String getCreatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        return createdAt.format(formatter);
    }
}