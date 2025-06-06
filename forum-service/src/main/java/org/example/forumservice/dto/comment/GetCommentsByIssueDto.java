package org.example.forumservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.forumservice.util.issue.IssueExists;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetCommentsByIssueDto implements Serializable {
    @IssueExists
    private final Long issueId;
    @UserExists
    private final String username;
}
