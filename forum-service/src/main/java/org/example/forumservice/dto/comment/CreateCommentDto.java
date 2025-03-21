package org.example.forumservice.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.forumservice.entity.Comment;

import java.io.Serializable;

/**
 * DTO for {@link Comment}
 */

@Builder
@AllArgsConstructor
@Getter
public class CreateCommentDto implements Serializable {
    @NotEmpty
    private final String content;
    @NotNull
    private final Long issueId;
    @NotEmpty
    private final String authorUsername;
}