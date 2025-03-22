package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@Builder
@AllArgsConstructor
public class GetIssueByIdApiDto implements Serializable {
    private final Long issueId;
}