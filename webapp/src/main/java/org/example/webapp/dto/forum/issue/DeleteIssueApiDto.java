package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Builder
@AllArgsConstructor
public class DeleteIssueApiDto implements Serializable {
    private final Long issueId;
    private final String username;
}