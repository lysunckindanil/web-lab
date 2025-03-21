package org.example.forumservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.forumservice.entity.Comment}
 */

@Builder
@AllArgsConstructor
public class CommentDto implements Serializable {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final String authorUsername;
}