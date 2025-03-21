package org.example.forumservice.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.entity.Comment;

import java.io.Serializable;

/**
 * DTO for {@link Comment}
 */

@Builder
@AllArgsConstructor
@Getter
public class DeleteCommentDto implements Serializable {
    @NotNull
    private final Long commentId;
    @NotEmpty
    private final String username;
}