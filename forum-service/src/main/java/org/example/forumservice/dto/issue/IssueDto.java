package org.example.forumservice.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.forumservice.model.Issue}
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IssueDto implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final String authorUsername;
    private final Boolean canDelete;
}