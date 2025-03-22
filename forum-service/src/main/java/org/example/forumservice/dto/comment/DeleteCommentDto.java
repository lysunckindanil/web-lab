package org.example.forumservice.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.model.Comment;
import org.example.forumservice.util.comment.CommentExists;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

/**
 * DTO for {@link Comment}
 */

@Builder
@AllArgsConstructor
@Getter
public class DeleteCommentDto implements Serializable {
    @CommentExists
    private final Long commentId;
    @NotEmpty
    @UserExists
    private final String username;
}