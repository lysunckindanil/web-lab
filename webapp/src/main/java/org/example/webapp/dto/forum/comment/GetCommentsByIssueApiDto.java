package org.example.webapp.dto.forum.comment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GetCommentsByIssueApiDto implements Serializable {
    private final Long issueId;
}
