package org.example.webapp.dto.forum.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
public class IssueDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String authorUsername;

    @JsonIgnore
    private boolean canDelete;

    public String getCreatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        return createdAt.format(formatter);
    }
}