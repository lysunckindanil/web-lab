package org.example.forumservice.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.forumservice.entity.Issue}
 */

@Builder
@AllArgsConstructor
public class IssueDto implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final String authorUsername;
}