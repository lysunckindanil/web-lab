package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@Builder
@AllArgsConstructor
public class GetCommentsByIssueApiDto implements Serializable {
    private final Long issueId;
}
