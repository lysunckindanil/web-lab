package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@Getter
public class DeleteIssueDto implements Serializable {
    private final Long issueId;
}