package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.forumservice.util.issue.IssueExists;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.model.Issue}
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeleteIssueDto implements Serializable {
    @IssueExists
    private final Long issueId;
    @NotEmpty
    @UserExists
    private final String username;
}