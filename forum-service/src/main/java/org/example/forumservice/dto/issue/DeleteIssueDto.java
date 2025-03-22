package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.util.issue.IssueExists;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.model.Issue}
 */

@Builder
@AllArgsConstructor
@Getter
public class DeleteIssueDto implements Serializable {
    @IssueExists
    private final Long issueId;
    @NotEmpty
    @UserExists
    private final String username;
}