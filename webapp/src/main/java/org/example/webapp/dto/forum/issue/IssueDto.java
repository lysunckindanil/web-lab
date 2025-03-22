package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Builder
@Getter
@AllArgsConstructor
public class IssueDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String authorUsername;

    public String getCreatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        return createdAt.format(formatter);
    }
}