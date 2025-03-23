package org.example.webapp.dto.forum.issue;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class DeleteIssueApiDto implements Serializable {
    private final Long issueId;
    private final String username;
}