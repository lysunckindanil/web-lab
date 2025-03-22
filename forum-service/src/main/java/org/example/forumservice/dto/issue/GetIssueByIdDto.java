package org.example.forumservice.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.util.issue.IssueExists;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@Getter
public class GetIssueByIdDto implements Serializable {
    @IssueExists
    private final Long issueId;
}