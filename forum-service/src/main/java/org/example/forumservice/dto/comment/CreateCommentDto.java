package org.example.forumservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.model.Comment;
import org.example.forumservice.util.issue.IssueExists;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

/**
 * DTO for {@link Comment}
 */

@Builder
@AllArgsConstructor
@Getter
public class CreateCommentDto implements Serializable {
    @NotBlank(message = "Содержание комментария не должно быть пустым")
    private final String content;
    @IssueExists
    private final Long issueId;
    @NotEmpty
    @UserExists
    private final String authorUsername;
}