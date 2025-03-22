package org.example.forumservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.util.issue.IssueExists;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class GetCommentsByIssueDto implements Serializable {
    @IssueExists
    private final Long issueId;
}
